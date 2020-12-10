package com.example.sonerimtestproject.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sonerimtestproject.R;
import com.example.sonerimtestproject.interactor.MapFragmentInteractor;
import com.example.sonerimtestproject.model.GeoPointModel;
import com.example.sonerimtestproject.model.LayerModel;
import com.example.sonerimtestproject.model.MyMarker;
import com.example.sonerimtestproject.presenter.MapFragmentPresenter;
import com.example.sonerimtestproject.utils.MapType;
import com.example.sonerimtestproject.utils.MyBingMapTileSource;
import com.example.sonerimtestproject.view.contract.MapFragmentContract;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.example.sonerimtestproject.utils.Constants.MAX_ZOOM;
import static com.example.sonerimtestproject.utils.Constants.MIN_ZOOM;
import static com.example.sonerimtestproject.utils.DrawableUtils.convertPointModelToPointGeo;
import static com.example.sonerimtestproject.utils.DrawableUtils.getDrawableFromVectorWithCustomColor;

public class MapFragment extends Fragment implements MapFragmentContract.View {

    protected static final int DEFAULT_INACTIVITY_DELAY_IN_MILLISECS = 200;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @BindView(R.id.fragment_map_view)
    MapView mMapView;
    @BindView(R.id.fab_zoom_plus)
    FloatingActionButton fabPlus;
    @BindView(R.id.fab_zoom_minus)
    FloatingActionButton fabMinus;
    @BindView(R.id.fab_map_fragment_filters)
    FloatingActionButton mFiltersFloatingActionButton;
    @BindView(R.id.fab_refresh_markers)
    FloatingActionButton mRefreshMarkers;
    @BindView(R.id.cl_custom_menu_filter)
    LinearLayout mFiltersLayout;

    @BindView(R.id.ib_default_map)
    ImageView mDefaultImageView;
    @BindView(R.id.ib_terrain_map)
    ImageView mTerrainImageView;
    @BindView(R.id.tv_terrain_map)
    TextView mTerrainTextView;
    @BindView(R.id.tv_default_map)
    TextView mDefaultTextView;

    private View mView;
    private MapFragmentPresenter presenter;
    private Map<String, MyMarker> mMapMarkers;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment, container, false);

        ButterKnife.bind(this, mView);

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        initData();

        setKeepScreenOn();

        initMap();

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });

        mDefaultImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryRed)));
        mTerrainImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        mTerrainTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mDefaultTextView.setTextColor(getResources().getColor(R.color.colorPrimaryRed));


        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onPause();
        presenter.dropView();
    }


    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void setKeepScreenOn() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void initData() {
        mMapMarkers = new HashMap<>();
        presenter = new MapFragmentPresenter(new MapFragmentInteractor());
    }

    @Override
    public void showMarkers(List<LayerModel> modelList) {
        if (mMapMarkers.size() > 0)
            deleteMarkers();
        for (LayerModel model : modelList) {
            setMapLayer(model);
        }
    }

    private void deleteMarkers() {
        for (MyMarker marker : mMapMarkers.values()) {
            mMapView.getOverlays().remove(marker);
            mMapView.getOverlay().clear();
        }
        mMapMarkers = new HashMap<>();
        mMapView.invalidate();
    }

    @Override
    public void showToast(String string) {

    }

    @Override
    public void setMapLayer(LayerModel layerModel) {
        if (layerModel.getCoordinate() == null) return;

        MyMarker marker = mMapMarkers.get(String.valueOf(layerModel.getId()));
        if (marker == null || !mMapView.getOverlays().contains(marker)) {
            marker = new MyMarker(mMapView);
            marker.setId(String.valueOf(layerModel.getId()));
            marker.setFlat(true);

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setLayerModel(layerModel);
            marker.setPosition(convertPointModelToPointGeo(layerModel.getCoordinate()));
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                if (marker1.isInfoWindowShown())
                    marker1.closeInfoWindow();
                else
                    marker1.showInfoWindow();

                return true;
            });
            Bitmap bitmap = makeBitmap(getContext(), String.valueOf(layerModel.getValueForUi()));

            if (bitmap != null)
                marker.setIcon(new BitmapDrawable(getResources(), bitmap));
            mMapMarkers.put(String.valueOf(layerModel.getId()), marker);
            mMapView.getOverlays().add(marker);

        } else {
            mMapView.getOverlays().remove(marker);
            mMapMarkers.remove(marker);
            mMapView.invalidate();
            setMapLayer(layerModel);
        }
        mMapView.invalidate();
    }

    public Bitmap makeBitmap(Context context, String text) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Drawable drawable = resources.getDrawable(R.drawable.ic_marker);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            bitmap = bitmap.copy(ARGB_8888, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK); // Text color
            paint.setTextSize(10 * scale); // Text size
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // Text shadow
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
//
            int x = bitmap.getWidth() - bounds.width() - 10; // 10 for padding from right
            int y = bounds.height();

            drawable.setBounds(-12, -12, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            canvas.drawText(text, x - 12, y + 12, paint);
        }
        return bitmap;
    }


    @OnClick(R.id.fab_map_fragment_filters)
    void setFilterOnClick() {
        if (mFiltersLayout.getVisibility() == View.GONE)
            mFiltersLayout.setVisibility(View.VISIBLE);
        else mFiltersLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.fab_refresh_markers)
    void setRefreshOnClick() {
        presenter.loadRandomMarkers();
    }

    @OnClick(R.id.ib_terrain_map)
    void onTerrainOnClick() {
        mMapView.setTileSource(MapType.OPEN_STREET_MAP_USGS_SAT.getTileSource());
        mDefaultImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        mTerrainImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryRed)));
        mTerrainTextView.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
        mDefaultTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mFiltersLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.ib_default_map)
    void onOpenStreetOnClick() {
        mMapView.setTileSource(MapType.OPEN_STREET_MAP.getTileSource());
        mDefaultImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryRed)));
        mTerrainImageView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        mTerrainTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mDefaultTextView.setTextColor(getResources().getColor(R.color.colorPrimaryRed));
        mFiltersLayout.setVisibility(View.GONE);
    }

    private void initMap() {
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (event.getZoomLevel() <= MAX_ZOOM && event.getZoomLevel() >= MIN_ZOOM) {

                }
                return true;
            }
        }, DEFAULT_INACTIVITY_DELAY_IN_MILLISECS));
        mMapView.setBuiltInZoomControls(false);
        mMapView.setMultiTouchControls(true);
        mMapView.setTilesScaledToDpi(true);
        mMapView.setZoomRounding(false);
        mMapView.setMaxZoomLevel(MAX_ZOOM);
        mMapView.setMinZoomLevel(MIN_ZOOM);
        mMapView.getController().setZoom(MIN_ZOOM);
        mMapView.setOnTouchListener((view, motionEvent) -> {
            mFiltersLayout.setVisibility(View.GONE);
            InfoWindow.closeAllInfoWindowsOn(mMapView);
            return false;
        });
    }

    @OnClick({R.id.fab_zoom_plus,
            R.id.fab_zoom_minus})
    void onClickButton(View view) {
        if (view.getId() == R.id.fab_zoom_plus) {
            mMapView.getController().zoomIn();
            mFiltersLayout.setVisibility(View.GONE);
        } else if (view.getId() == R.id.fab_zoom_minus) {
            updateOnMapMarkers();
            mMapView.getController().zoomOut();
            mFiltersLayout.setVisibility(View.GONE);
        }
    }

    private void updateOnMapMarkers() {
        double zoomDelta = ((MAX_ZOOM - mMapView.getZoomLevelDouble()) * 24) / 1000;
        List<LayerModel> layerModelList = new ArrayList<>();
        List<MyMarker> myMarkerList = new ArrayList<>();
        myMarkerList.addAll(mMapMarkers.values());
        List<MyMarker> inerMarkerList = new ArrayList<>(myMarkerList);
        List<MyMarker> removeList = null;
        List<MyMarker> globalRemoveList = new ArrayList<>();
        double distance = 0;
        for (int i = 0; i < myMarkerList.size(); i++) {
            removeList = new ArrayList<>();
            for (int j = 1; j < inerMarkerList.size(); j++) {
                distance = myMarkerList.get(i).getLayerModel().getCoordinate().getDistance(inerMarkerList.get(j).getLayerModel().getCoordinate());
                if (zoomDelta > distance) {
                    myMarkerList.get(i).setLayerModel(new LayerModel(myMarkerList.get(i).getLayerModel().getId(), Color.RED,
                            getCenterOfCoordinates(myMarkerList.get(i).getLayerModel().getCoordinate(), inerMarkerList.get(j).getLayerModel().getCoordinate()),
                            (myMarkerList.get(i).getLayerModel().getValueForUi() + inerMarkerList.get(j).getLayerModel().getValueForUi()) / 2));
                    removeList.add(inerMarkerList.get(j));
                    globalRemoveList.add(inerMarkerList.get(j));
                } else {
                    globalRemoveList.remove(inerMarkerList.get(j));
//                    layerModelList.add(inerMarkerList.get(j).getLayerModel());
                }
            }
            inerMarkerList.remove(myMarkerList.get(i));
            inerMarkerList.removeAll(removeList);
        }

        myMarkerList.removeAll(globalRemoveList);

        for (
                MyMarker marker : myMarkerList)
            layerModelList.add(marker.getLayerModel());

        showMarkers(layerModelList);

    }

    private GeoPointModel getCenterOfCoordinates(GeoPointModel first, GeoPointModel second) {
        double latitude = (first.getLatitude() + second.getLatitude()) / 2.;
        double longitude = (first.getLongitude() + second.getLongitude()) / 2.;

        return new GeoPointModel(latitude, longitude);
    }
}
