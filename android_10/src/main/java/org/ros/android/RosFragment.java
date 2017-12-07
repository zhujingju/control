package org.ros.android;

//import android.app.Fragment;

import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.google.common.base.Preconditions;

import org.ros.address.InetAddressFactory;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Administrator on 2017/8/28-028.
 */

public abstract class RosFragment extends Fragment {
    private static final String TAG = "Print-RosFra";
    private static final int MASTER_CHOOSER_REQUEST_CODE = 0;

    private ServiceConnection nodeMainExecutorServiceConnection;//final
    private final String notificationTicker;
    private final String notificationTitle;
    private URI customMasterUri_ = null;
    protected NodeMainExecutorService nodeMainExecutorService;
    private boolean isBindService = false;

    private Context mContext;

    private final class NodeMainExecutorServiceConnection implements ServiceConnection {

        private URI customMasterUri;

        public NodeMainExecutorServiceConnection(URI customUri) {
            super();
            Log.i(TAG, "NodeMainExecutorServiceConnection() ");
            customMasterUri = customUri;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG, "onServiceConnected() ");
            nodeMainExecutorService = ((NodeMainExecutorService.LocalBinder) binder).getService();

            if (customMasterUri != null) {
                nodeMainExecutorService.setMasterUri(customMasterUri);
                nodeMainExecutorService.setRosHostname(getDefaultHostAddress());
            }
            nodeMainExecutorService.addListener(new NodeMainExecutorServiceListener() {
                @Override
                public void onShutdown(NodeMainExecutorService nodeMainExecutorService) {
                    // We may have added multiple shutdown listeners and we only want to
                    // call finish() once.
//                    if (!RosFragment.this.isFinishing()) {
//                        RosFragment.this.finish();
//                    }
                    serviceShutdown(nodeMainExecutorService);
                    Log.e(TAG, "onShutdown: nodeMainExecutorService is shut down!!!");
                }
            });
            if (getMasterUri() == null) {
                startMasterChooser();
            } else {
                init();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected() ");
        }
    };

    protected RosFragment(String notificationTicker, String notificationTitle) {
        this(notificationTicker, notificationTitle, null);
        Log.i(TAG, "RosActivity() ");
    }

    protected RosFragment(String notificationTicker, String notificationTitle, URI customMasterUri) {
        super();
        Log.i(TAG, "RosActivity()");
        isBindService = false;
        this.notificationTicker = notificationTicker;
        this.notificationTitle = notificationTitle;
        this.customMasterUri_ = customMasterUri;
        nodeMainExecutorServiceConnection = new NodeMainExecutorServiceConnection(customMasterUri_);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
//        nodeMainExecutorServiceConnection = new NodeMainExecutorServiceConnection(this.customMasterUri_);
//        bindNodeMainExecutorService();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        bindNodeMainExecutorService();
    }

    protected void bindNodeMainExecutorService() {
        Log.i(TAG, "bindNodeMainExecutorService()");
        Intent intent = new Intent(mContext, NodeMainExecutorService.class);
        intent.setAction(NodeMainExecutorService.ACTION_START);
        intent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TICKER, notificationTicker);
        intent.putExtra(NodeMainExecutorService.EXTRA_NOTIFICATION_TITLE, notificationTitle);
        mContext.startService(intent);
        Preconditions.checkState(
                mContext.bindService(intent, nodeMainExecutorServiceConnection, BIND_AUTO_CREATE),
                "Failed to bind NodeMainExecutorService.");
        isBindService = true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy(), isBindService="+isBindService);
        if(isBindService) mContext.unbindService(nodeMainExecutorServiceConnection);
        super.onDestroy();
    }

    protected void init() {
        Log.i(TAG, "init()");
        // Run init() in a new thread as a convenience since it often requires
        // network access.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                init(nodeMainExecutorService);
                return null;
            }
        }.execute();
    }

    /**
     * This method is called in a background thread once this {@link Fragment} has
     * been initialized with a master {@link URI} via the {@link MasterChooser}
     * and a {@link NodeMainExecutorService} has started. Your {@link org.ros.node.NodeMain}s
     * should be started here using the provided {@link NodeMainExecutor}.
     *
     * @param nodeMainExecutor
     *          the {@link NodeMainExecutor} created for this {@link Fragment}
     */
    protected abstract void init(NodeMainExecutor nodeMainExecutor);
    protected abstract void serviceShutdown(NodeMainExecutorService nodeMainExecutorService);


    public URI getMasterUri() {
        Log.i(TAG, "getMasterUri()");
        Preconditions.checkNotNull(nodeMainExecutorService);
        return nodeMainExecutorService.getMasterUri();
    }

    public String getRosHostname() {
        Log.i(TAG, "getRosHostname()");
        Preconditions.checkNotNull(nodeMainExecutorService);
        return nodeMainExecutorService.getRosHostname();
    }
    protected abstract void startMasterChooser();

    private String getDefaultHostAddress() {
        return InetAddressFactory.newNonLoopback().getHostAddress();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
