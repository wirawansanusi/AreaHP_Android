package com.wirawansanusi.areahp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wirawansanusi.areahp.R;
import com.wirawansanusi.areahp.classes.ContactsAdapter;
import com.wirawansanusi.areahp.model.Contacts;

public class ContactActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Contacts[] contacts = new Contacts[] {
                new Contacts(R.drawable.direction, "Tunjukkan arah dari posisi sekarang", "maps", "-6.136808,106.824064"),
                new Contacts(R.drawable.phone, "085-777-11-8989", "phone", "085777118989"),
                new Contacts(R.drawable.bbm, "BBM PIN 2B683755", "bbm", "2B683755"),
                new Contacts(R.drawable.email, "Email areahp77@yahoo.com", "email", "areahp77@yahoo.com"),
                new Contacts(R.drawable.email, "Email areahp77@ymail.com", "email", "areahp77@ymail.com")
        };

        LinearLayout contactList = (LinearLayout) findViewById(R.id.listView);
        ContactsAdapter adapter = new ContactsAdapter(this, contacts);
        for (int i=0; i<contacts.length; i++) {
            View view = adapter.getView(i, null, null);
            contactList.addView(view);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(-6.136808, 106.824064);
        mMap.addMarker(new MarkerOptions().position(location).title("AreaHP"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
    }
}
