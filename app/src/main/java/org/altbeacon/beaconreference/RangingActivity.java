package org.altbeacon.beaconreference;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.ArrayList;
import java.util.List;

public class RangingActivity extends ListActivity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    private ListView listView;
    BeaconAdapter adapter;
    private List<String> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ranging);

//        listView = (ListView) findViewById(R.id.beaconList);
        adapter = new BeaconAdapter(this, R.layout.beacon_list_item, infoList);
        setListAdapter(adapter);

        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier((beacons, region) -> {

            infoList.clear();
            for (Beacon beacon : beacons) {
                infoList.add(getInfo(beacon));
            }

            adapter.refresh(infoList);
            runOnUiThread(()->adapter.notifyDataSetChanged());

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) { }
    }


    private String getInfo(Beacon beacon) {

        String url;
        if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10)
            url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
        else
            url = "Type: " + beacon.getBeaconTypeCode();

        String s = String.format("Name: '%s', Distance: %.2f metres away, Power: %d db\n\t %s", beacon.getBluetoothName(), beacon.getDistance(), beacon.getTxPower(), url);
        return s;
    }
}
