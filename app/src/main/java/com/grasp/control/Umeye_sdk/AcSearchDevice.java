 package com.grasp.control.Umeye_sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Player.Core.PlayerClient;
import com.Player.Source.TSearchDev;
import com.grasp.control.MyInterface.AddListener;
import com.grasp.control.MyInterface.AddRquipmentListent;
import com.grasp.control.R;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.tool.MyApplication;
import com.swipemenulistview.SwipeMenu;
import com.swipemenulistview.SwipeMenuCreator;
import com.swipemenulistview.SwipeMenuItem;
import com.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class AcSearchDevice extends Fragment {
	public ShowProgress pd;
	private SwipeMenuListView listView;
	private SwipeMenuListView toulistView;
	private List<Goods> toulist ;
	private myListViewAdapter adapter;
	private SearchDeviceAdapter sAdapter;
	public static ArrayList<SearchDeviceInfo> list;

	private MyApplication appMain;

	public static  boolean AcSearchDevice_zt=false;

    private Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ac_search_devce,container,false);
        context=getActivity();
        AcSearchDevice_zt=true;
        initView(view);
        return view;
    }


	public void initView(View view) {
		appMain = (MyApplication)((Activity) context).getApplication();

//		sp= PreferenceManager.getDefaultSharedPreferences(context);
//		Uid=sp.getString(DemoApplication.UID, "");

		initlistView(view);
		listView = (SwipeMenuListView) view.findViewById(R.id.lvLive);
//		listView.setVisibility(View.INVISIBLE);
		sAdapter = new SearchDeviceAdapter(context);
		View la=LayoutInflater.from(context).inflate(R.layout.list_qita, null);
		listView.addFooterView(la);
		listView.setAdapter(sAdapter);

		listView.setCacheColorHint(0);
		listView.setonRefreshListener(new SwipeMenuListView.OnRefreshListener() { //刷新

			@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
			@Override
			public void onRefresh() {

				th2=new ThreadSearchDevice2();
				th2.execute();

			}
		});



		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				arg2-=1;
//				Toast.makeText(AcSearchDevice.this,arg2+" ",Toast.LENGTH_SHORT).show();
				if(arg2==list.size()){
//					Toast.makeText(AcSearchDevice.this,"qt",Toast.LENGTH_SHORT).show();
//					startActivityForResult(new Intent(AcSearchDevice.this, OtherActivity.class),100);
                    addListener.AddRquipmentListent("");
				}
			}
		});

//		findViewById(R.id.menu_btn1).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new ThreadSearchDevice().execute();
//			}
//		});
		th=new ThreadSearchDevice();
		th.execute();
	}



	private ThreadSearchDevice th;
	private ThreadSearchDevice2 th2;
	private void initlistView(View view){
		toulistView = (SwipeMenuListView) view.findViewById(R.id.smlist);
		toulistView.setCacheColorHint(0);
		adapter=new myListViewAdapter(context, toulist);
		dataListview();
		toulistView.setAdapter(adapter);



        SwipeMenuCreator creator = new SwipeMenuCreator() {



            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//				SwipeMenuItem openItem = new SwipeMenuItem(
//						getApplicationContext());
                // set item background
//				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//						0xCE)));
//				// set item width
//				openItem.setWidth(dp2px(90));
//				// set item title
//				openItem.setTitle("Open");
//				// set item title fontsize
//				openItem.setTitleSize(18);
//				// set item title font color
//				openItem.setTitleColor(Color.WHITE);
//				// add to menu
//				menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(70));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        toulistView.setMenuCreator(creator);

        // step 2. listener item click event
        toulistView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {



            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//				Emp_layout_1.Goods item = toulist.get(position);
                switch (index) {
//				case 0:
//					// open
//					open(item);
//					break;
                    case 0:
                        // delete
//					delete(item);
//					list.remove(position);
//					adapter.notifyDataSetChanged();

                        posi=position;
                        ha.sendEmptyMessage(888);




                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        toulistView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

	}

	Handler ha=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 888:
					del();
					break;
                case 999:
                    adapter.setList(toulist);
                    adapter.notifyDataSetChanged();
                    ss_zt=true;
                    if(addListener!=null){
                        addListener.SetRquipmentListent();
                    }

                    break;
			}
		}
	};


	private void dataListview(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                toulist=new ArrayList<Goods>();

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s="select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Goods g=new Goods();
                    g.setName(uid);
                    toulist.add(g);
                    Log.d("qqq","sid="+sid);

                }
                ha.sendEmptyMessage(999);
            }
        }).start();



	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	private int posi=0;
	private void del(){   //删除

		AlertDialog.Builder builder = new AlertDialog.Builder(
				context).setTitle(getString(R.string.shuru2) + "“"
				+ toulist.get(posi).getName() + "”");
		builder.setPositiveButton(getString(R.string.alert_dialog_ok),new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				delData();
			}
		});

		builder.setNegativeButton(
				getString(R.string.alert_dialog_cancel), null);
		builder.show();
	}

	private boolean ss_zt=false;

	private void delData() {
        AddSQLiteHelper dbHelper = new AddSQLiteHelper(MyApplication.getContext(), "add.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String s="delete from  AddEquipment    where uid='"+toulist.get(posi).getName()+"'";
        db.execSQL(s);
        Toast.makeText(context,R.string.progress_pergood,Toast.LENGTH_SHORT).show();
        dataListview();
	}


	@Override
	public void onResume() {
		Log.d("qqq","onResume");
		if (sAdapter != null) {
			sAdapter.notifyDataSetChanged();
		}
//		if(adapter!=null){
//			dataListview();
//		}
		super.onResume();

	}

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		Log.d("qqq","onNewIntent");
//		if(adapter!=null){
//			dataListview();
//		}
//	}

	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	@Override
	public  void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
        ha.removeCallbacks(null);
		AcSearchDevice_zt=false;
		if(th != null && th.getStatus() == AsyncTask.Status.RUNNING){
			th.cancel(true);
		}
		if(th2 != null && th2.getStatus() == AsyncTask.Status.RUNNING){
			th2.cancel(true);
		}
	}



	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	public class ThreadSearchDevice extends
			AsyncTask<Void, Void, List<SearchDeviceInfo>> {

		@Override
		protected List<SearchDeviceInfo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			list = new ArrayList<SearchDeviceInfo>();
			// String[] temp = StreamData.ServerAddress.split(":");
			// String address = temp[0];
			// int port = Integer.parseInt(temp[1]);
			// String userName = StreamData.UserName;
			// String password = StreamData.Password;
			// System.out.println(address + ":" + port + "  " + userName + "  "
			// + password);
			PlayerClient playerclient = appMain.getPlayerclient();
			int searchRet = playerclient.StartSearchDev(10);// 5代表等待多少秒
			for (int i = 0; i < searchRet; i++) {
				TSearchDev tsearch = playerclient.SearchDevByIndex(i);

				SearchDeviceInfo searchInfo = new SearchDeviceInfo(
						tsearch.dwVendorId, tsearch.sDevName, tsearch.sDevId,
						tsearch.sDevUserName, tsearch.bIfSetPwd,
						tsearch.bIfEnableDhcp, tsearch.sAdapterName_1,
						tsearch.sAdapterMac_1, tsearch.sIpaddr_1,
						tsearch.sNetmask_1, tsearch.sGateway_1,
						tsearch.usChNum, tsearch.iDevPort, tsearch.sDevModel,
						tsearch.currentIp, tsearch.connectState,
						tsearch.iSrvConnResult);
				Log.w("searchRet", "UMId :" + searchInfo.toString());
				list.add(searchInfo);

			}
			playerclient.StopSearchDev();
			return list;
		}

		@Override
		protected void onPostExecute(List<SearchDeviceInfo> flist) {
			// TODO Auto-generated method stub
			pd.dismiss();
			if (list.size() > 0) {
//				listView.setVisibility(View.VISIBLE);
				sAdapter.setNodeList(flist);
				// listView.startLayoutAnimation();
			} else {
//				listView.setVisibility(View.INVISIBLE);
//				SearchDeviceInfo a=new SearchDeviceInfo(2,"aaaa","aaaa","aaaa",2,2,"aaaa","aaaa","aaaa","aaaa","aaaa",2,2,"aaa","aaaa",2,2);
//				list = new ArrayList<SearchDeviceInfo>();
//				list.add(a);
//				list.add(a);
//				list.add(a);
//				list.add(a);
//				list.add(a);
//				list.add(a);
//				list.add(a);
				sAdapter.setNodeList(flist);
				Show.toast(context, R.string.nodataerro);
			}

			super.onPostExecute(list);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (pd == null) {
				pd = new ShowProgress(context);
				pd.setMessage(AcSearchDevice.this.getResources().getString(
						R.string.searching_device));
				pd.setCanceledOnTouchOutside(true);
			}
			if(pd!=null){
                pd.show();
            }

			super.onPreExecute();
		}
	}


	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	public class ThreadSearchDevice2 extends
			AsyncTask<Void, Void, List<SearchDeviceInfo>> {

		@Override
		protected List<SearchDeviceInfo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			list = new ArrayList<SearchDeviceInfo>();
			// String[] temp = StreamData.ServerAddress.split(":");
			// String address = temp[0];
			// int port = Integer.parseInt(temp[1]);
			// String userName = StreamData.UserName;
			// String password = StreamData.Password;
			// System.out.println(address + ":" + port + "  " + userName + "  "
			// + password);
			PlayerClient playerclient = appMain.getPlayerclient();
			int searchRet = playerclient.StartSearchDev(10);// 5代表等待多少秒
			for (int i = 0; i < searchRet; i++) {
				TSearchDev tsearch = playerclient.SearchDevByIndex(i);

				SearchDeviceInfo searchInfo = new SearchDeviceInfo(
						tsearch.dwVendorId, tsearch.sDevName, tsearch.sDevId,
						tsearch.sDevUserName, tsearch.bIfSetPwd,
						tsearch.bIfEnableDhcp, tsearch.sAdapterName_1,
						tsearch.sAdapterMac_1, tsearch.sIpaddr_1,
						tsearch.sNetmask_1, tsearch.sGateway_1,
						tsearch.usChNum, tsearch.iDevPort, tsearch.sDevModel,
						tsearch.currentIp, tsearch.connectState,
						tsearch.iSrvConnResult);
				Log.w("searchRet", "UMId :" + searchInfo.toString());
				list.add(searchInfo);

			}
			playerclient.StopSearchDev();
			return list;
		}

		@Override
		protected void onPostExecute(List<SearchDeviceInfo> flist) {
			// TODO Auto-generated method stub
//			pd.dismiss();
			if (list.size() > 0) {
//				listView.setVisibility(View.VISIBLE);
				sAdapter.setNodeList(flist);
				// listView.startLayoutAnimation();
			} else {
//				listView.setVisibility(View.INVISIBLE);
				sAdapter.setNodeList(flist);
				Show.toast(context, R.string.nodataerro);
			}
			listView.onRefreshComplete();
			super.onPostExecute(list);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
//			if (pd == null) {
//				pd = new ShowProgress(AcSearchDevice.this);
//				pd.setMessage(AcSearchDevice.this.getResources().getString(
//						R.string.searching_device));
//				pd.setCanceledOnTouchOutside(false);
//			}
//			pd.show();
			super.onPreExecute();
		}
	}



	class SearchDeviceAdapter extends BaseAdapter {
        public static final int MODIFY_DIR_SUCCESS = 4;
        public static final int MODIFY_DIR_FIALED = 5;
        private List<SearchDeviceInfo> nodeList;
        private Context con;
        private LayoutInflater inflater;
        // View view;
        public TextView txtParameters, txtName, txtDelete;
        int currentPosition;
        public ProgressDialog progressDialog;
        public boolean parentIsDvr = false;
        private SharedPreferences sp;

        public SearchDeviceAdapter(Context con) {
            this.con = con;
            inflater = LayoutInflater.from(con);
            nodeList = new ArrayList<SearchDeviceInfo>();
            sp = PreferenceManager.getDefaultSharedPreferences(con);
            // editor = con.getSharedPreferences(FgFavorite.fileName,
            // Context.MODE_PRIVATE).edit();
        }

        public List<SearchDeviceInfo> getNodeList() {
            return nodeList;
        }

        public void setNodeList(List<SearchDeviceInfo> nodeList) {
            this.nodeList = nodeList;
            notifyDataSetChanged();
        }

        public boolean isParentIsDvr() {
            return parentIsDvr;
        }

        public void setParentIsDvr(boolean parentIsDvr) {
            this.parentIsDvr = parentIsDvr;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return nodeList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            SearchDeviceInfo node = nodeList.get(position);
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = inflater.inflate(R.layout.search_device_item, null);
                vh.tv = (TextView) convertView.findViewById(R.id.tvCaption);
                vh.info = (TextView) convertView.findViewById(R.id.tvInfo);

                vh.add = (Button) convertView.findViewById(R.id.btn_add);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
//			vh.tv.setText(node.currentIp + "  " + node.getsDevId() + "  "
//					+ node.usChNum);
            vh.tv.setText(node.getsDevId());
            vh.info.setText(node.serverState);
            OnClickListstener clickListener = new OnClickListstener(node, position);
            // vh.imgaArrow.setOnClickListener(clickListener);
            vh.add.setOnClickListener(clickListener);
            return convertView;
        }

        class ViewHolder {
            TextView tv;
            TextView info;
            Button add;
        }

        public class OnClickListstener implements OnClickListener

        {
            SearchDeviceInfo node;
            int position;

            public OnClickListstener(SearchDeviceInfo node, int position) {
                this.node = node;
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_add) {

//				Activity activity = (Activity) con;
//				Intent intent = new Intent(con, Robote_add_activity.class);
//					intent.putExtra("umid", node.getsDevId());
//				activity.startActivityForResult(intent,100);

                    addListener.AddRquipmentListent(node.getsDevId());
                }
            }
        }
    }

        class myListViewAdapter extends BaseAdapter {

            LayoutInflater inflater = null;
            private Context context;
            private List<Goods> list = null;

            public myListViewAdapter(Context context, List<Goods> list) {
                this.context = context;
                this.list = list;
                inflater = ((Activity) (context)).getLayoutInflater();
            }


            public List<Goods> getList() {
                return list;
            }


            public void setList(List<Goods> list) {
                this.list = list;
            }


            @Override
            public int getCount() {
                if (list != null) {
                    return list.size();
                }
                return 0;
            }

            @Override
            public Goods getItem(int arg0) {
                if (list != null) {
                    return list.get(arg0);
                }
                return null;
            }

            @Override
            public long getItemId(int arg0) {
                return arg0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = convertView;
                final Goods camera = getItem(position);
                final ListViewTool too;
                if (convertView == null) {
                    view = inflater.inflate(R.layout.sw_item, parent, false);
                    too = new ListViewTool();

                    too.name = (TextView) view.findViewById(R.id.ybd_tv);
                    too.last = (LinearLayout) view.findViewById(R.id.sw_item_last);


                    view.setTag(too);
                } else {
                    too = (ListViewTool) view.getTag();
                }
                if (camera == null) return null;
                too.name.setText(camera.getName());
                too.last.setBackgroundResource(R.drawable.d1);
                if (list.size() > 1) {
                    if (position == list.size() - 1) {
                        too.last.setBackgroundResource(R.drawable.d2);
                    } else {

                    }
                } else {

                    too.last.setBackgroundResource(R.drawable.d2);

                }

//			if(camera.getName().equals(Uid)){
//				too.ss.setState(true);
//			}

                Log.d("qqq", camera.getName() + "");

                return view;
            }

            class ListViewTool {

                public LinearLayout last;
                public TextView name;
            }
        }


        class Goods {
            private String name;
            private boolean zt, szt;
            private int num;

            public boolean isSzt() {
                return szt;
            }

            public void setSzt(boolean szt) {
                this.szt = szt;
            }

            public boolean getZt() {
                return zt;
            }

            public void setZt(boolean zt) {
                this.zt = zt;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isZt() {
                return zt;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }




    private AddRquipmentListent addListener;

    public void setAddListener(AddRquipmentListent addListener) {
        this.addListener = addListener;
    }

}
