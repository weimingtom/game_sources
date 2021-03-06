package com.haowan123.kof.uc;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.ninegame.game.GLog;
import com.ninegame.game.LAGameView;
import com.ninegame.ucgamesdk.*;
import com.ptola.GameUserData;
import com.ptola.GameWebViewBridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import cn.uc.gamesdk.GameUserLoginResult;
import cn.uc.gamesdk.IGameUserLogin;
import cn.uc.gamesdk.UCCallbackListener;
import cn.uc.gamesdk.UCCallbackListenerNullException;
import cn.uc.gamesdk.UCFloatButtonCreateException;
import cn.uc.gamesdk.UCGameSDK;
import cn.uc.gamesdk.UCGameSDKStatusCode;
import cn.uc.gamesdk.UCLogLevel;
import cn.uc.gamesdk.UCLoginFaceType;
import cn.uc.gamesdk.UCOrientation;
import cn.uc.gamesdk.info.FeatureSwitch;
import cn.uc.gamesdk.info.GameParamInfo;
import cn.uc.gamesdk.info.OrderInfo;
import cn.uc.gamesdk.info.PaymentInfo;

/**
 * 
 * 游戏主程序。包含了对UCGameSDK接口的调用。 必接功能：开机启动页（闪屏）、UCID登录、悬浮按钮、SDK充值（如未开启充值可不调用）、退出SDK<br>
 * 建议参考以下帖子进行接入，方便快捷、减少出错！<br>
 * 【SDK接入】接入注意事项汇总贴（必看）http://bbs.9game.cn/thread-3650324-1-1.html<br>
 * 【SDK接入】九游SDK必须接入的功能！务必查看！http://bbs.9game.cn/thread-3704011-1-1.html<br>
 * 
 * @author chenzh
 * 
 */
public class StartupActivity extends Activity {

	private LAGameView gameView;
	private Handler handler;
	private String m_SID;
	public void onCreate(Bundle b) {
		GLog.d("GameActivity", "----------onCreate---------");
		UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);
		super.onCreate(b);
		this.setContentView(R.layout.bg); // 设置启动画面

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				android.os.Message msg = new android.os.Message();
				msg.what = 10000;
				handler.sendMessage(msg);
			}
		};

		handler = new Handler()
		{
			public void handleMessage(android.os.Message msg)
			{
				switch(msg.what)
				{
					case 10000:
						dispalyGameLoginUI();
						break;
				}
			}
		};
		handler.postDelayed(runnable, 4500);
		//new Thread(runnable).start();
        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        */
	}

	/**
	 * 进行网络检查
	 */
	public boolean checkNetwork() {
		// !!!在调用SDK初始化前进行网络检查
		// 当前没有拥有网络
		if (false == APNUtil.isNetworkAvailable(this)) {
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setMessage("网络未连接,请设置网络");
			ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent("android.settings.SETTINGS");
					startActivityForResult(intent, 0);
				}
			});
			ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			ab.show();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 必接功能<br>
	 * sdk初始化功能<br>
	 */
	private void ucSdkInit() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", "正在初始化",
				true);
		dialog.setCancelable(false);

		// 检查网络连接，如无网络，则直接返回
		if (!checkNetwork()) {
			return;
		}

		// 监听用户注销登录消息
		// 九游社区-退出当前账号功能执行时会触发此监听
		try {
			UCGameSDK.defaultSDK().setLogoutNotifyListener(
					new UCCallbackListener<String>() {
						@Override
						public void callback(int statuscode, String msg) {
							// TODO 此处需要游戏客户端注销当前已经登录的游戏角色信息
							String s = "游戏接收到用户退出通知。" + msg + statuscode;
							Log.e("UCGameSDK", s);
							// 未成功初始化
							if (statuscode == UCGameSDKStatusCode.NO_INIT) {
								// 调用SDK初始化接口
								ucSdkInit();
							}
							// 未登录成功
							if (statuscode == UCGameSDKStatusCode.NO_LOGIN) {
								// 调用SDK登录接口
								ucSdkLogin();
							}
							// 退出账号成功
							if (statuscode == UCGameSDKStatusCode.SUCCESS) {
								// 调用SDK登录接口
								ucSdkLogin();
							}
							// 退出账号失败
							if (statuscode == UCGameSDKStatusCode.FAIL) {
								// 调用SDK退出当前账号接口
								ucSdkLogout();
							}
						}
					});
		} catch (UCCallbackListenerNullException e) {
			// 处理异常
		}

		try {
			GameParamInfo gpi = new GameParamInfo();// 下面的值仅供参考
			gpi.setCpId(UCSdkConfig.cpId);
			gpi.setGameId(UCSdkConfig.gameId);
			gpi.setServerId(UCSdkConfig.serverId);
			// gpi.setChannelId(2); // 渠道号统一处理，已不需设置，此参数已废弃，服务端此参数请设置值为2

			// 在九游社区设置显示查询充值历史和显示切换账号按钮，
			// 在不设置的情况下，默认情况情况下，生产环境显示查询充值历史记录按钮，不显示切换账户按钮
			// 测试环境设置无效
			gpi.setFeatureSwitch(new FeatureSwitch(true, false));

			// 设置SDK登录界面为横屏，个人中心及充值页面默认为强制竖屏，无法修改
			 //UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);

			// 设置SDK登录界面为竖屏
			//UCGameSDK.defaultSDK().setOrientation(UCOrientation.PORTRAIT);

			// 设置登录界面：
			// USE_WIDGET - 简版登录界面
			// USE_STANDARD - 标准版登录界面
			UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_WIDGET);

			// setUIStyle已过时，不需调用。
			// UCGameSDK.defaultSDK().setUIStyle(UCUIStyle.STANDARD);

			UCGameSDK.defaultSDK().initSDK(getApplicationContext(),
					UCLogLevel.ERROR, UCSdkConfig.debugMode, gpi,
					new UCCallbackListener<String>() {
						@Override
						public void callback(int code, String msg) {
							dialog.dismiss();
							Log.e("UCGameSDK", "UCGameSDK初始化接口返回数据 msg:" + msg
									+ ",code:" + code + ",debug:"
									+ UCSdkConfig.debugMode + "\n");
							switch (code) {
							// 初始化成功,可以执行后续的登录充值操作
							case UCGameSDKStatusCode.SUCCESS:
								// 调用sdk登录接口
								ucSdkLogin();
								break;

							// 初始化失败
							case UCGameSDKStatusCode.INIT_FAIL:
								// 调用sdk初始化接口
								ucSdkInit();
							default:
								break;
							}
						}
					});
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 模拟游戏老账号认证，并返回sid
	 */
	private String verifyGameUser(String username, String password) {
		String sid = "";
		// 此处需要进行游戏服务端登录校验，并从游戏服务端进行ucid.bind.create接口，进行UC账号绑定，获取对应的ucid和sid
		if (username.equals(password)) {
			sid = "0c3b8357-2b24-4b2f-97d9-c14ff687bd6d113550";
		} else {
			sid = "";
		}
		return sid;
	}


	/**
	 * 必接功能<br>
	 * SDK登录功能<br>
	 * SDK客户端登录成功后，游戏客户端通过getsid()方法获取SDK客户端的sid，发送给游戏服务器，
	 * 游戏服务器使用此sid进行服务端接口（ucid.user.sidInfo接口）调用，即可获取ucid及昵称，
	 * 随后游戏服务器向游戏客户端发送ucid及昵称即可。 （注：游戏客户端无法直接从SDK客户端获取UCID）
	 * 详细流程可见接入文档“02-技术文档-SDK总体机制\UC游戏_SDK_开发参考说明书_总体机制_vXXX.pdf”。
	 */
	private void ucSdkLogin() {
		final StartupActivity me = this;

		try {
			boolean gameAccountEnable = true; // 游戏老账号是否启用
			String gameAccountTitle = "拳皇咆哮"; // 游戏老账号标题。例如：“松鼠大战”，开发商可以自行设置。如已启用老账号登录功能，此参数不能为空。

			// 登录接口回调。从这里可以获取登录结果。
			UCCallbackListener<String> loginCallbackListener = new UCCallbackListener<String>() {
				@Override
				public void callback(int code, String msg) {
					
					Log.e("UCGameSDK", "UCGameSdk登录接口返回数据:code=" + code
							+ ",msg=" + msg);

					// 登录成功。此时可以获取sid。并使用sid进行游戏的登录逻辑。
					// 客户端无法直接获取UCID
					if (code == UCGameSDKStatusCode.SUCCESS) {

						// 获取sid。（注：ucid需要使用sid作为身份标识去SDK的服务器获取）
						m_SID = null;
						m_SID = UCGameSDK.defaultSDK().getSid();
						if(m_SID.length() > 0)
						{
							System.out.println("sid.lengthsid.length ：" + m_SID.length());
							System.out.println(m_SID);
							Runnable runnableCon = new Runnable() {
								@Override
								public void run() {
									confirmConnect(m_SID);
								}
							};
							
							new Thread(runnableCon).start();
						}
						
						
						System.out.println("可以连接");
						// 执行悬浮按钮创建及显示方法
						ucSdkFloatButton();
					}

					// 登录失败。应该先执行初始化成功后再进行登录调用。
					if (code == UCGameSDKStatusCode.NO_INIT) {
						// 没有初始化就进行登录调用，需要游戏调用SDK初始化方法
						ucSdkInit();
					}

					// 登录退出。该回调会在登录界面退出时执行。
					if (code == UCGameSDKStatusCode.LOGIN_EXIT) {
						// 登录界面关闭，游戏需判断此时是否已登录成功进行相应操作
					}
				}
			};

			// 启用游戏老账号登录
			if (gameAccountEnable) {
				// 游戏老账号登录钩子。游戏必须遵循该接口返回对应数据以实现登录。
				IGameUserLogin gameUserLoginHook = new IGameUserLogin() {
					@Override
					public GameUserLoginResult process(String userName,
							String passWord) {

						// !!! 这里只是模拟游戏老账号登录流程。
						GameUserLoginResult galr = new GameUserLoginResult();

						// 假设用户名与密码一致的时候游戏帐号认证通过。
						String sid = verifyGameUser(userName, passWord);
						if (sid != null && sid != "" && sid.length() > 0) {
							galr.setLoginResult(UCGameSDKStatusCode.SUCCESS);
							galr.setSid(sid);

						} else {
							galr.setLoginResult(UCGameSDKStatusCode.LOGIN_GAME_USER_AUTH_FAIL);
							galr.setSid("");
						}
						return galr;
					}
				};
				UCGameSDK.defaultSDK().login(me, loginCallbackListener,
						gameUserLoginHook, gameAccountTitle);
			}
			// 未启用官方老账号登录
			else {
				UCGameSDK.defaultSDK().login(me, loginCallbackListener);
			}
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 必接功能<br>
	 * 当游戏退出前必须调用该方法，进行清理工作。建议在游戏退出事件中进行调用，必须在游戏退出前执行<br>
	 * 如果游戏直接退出，而不调用该方法，可能会出现未知错误，导致程序崩溃<br>
	 */
	private void ucSdkExit() {
		UCGameSDK.defaultSDK().exitSDK();
	}

	/**
	 * 选接功能<br>
	 * 游戏可通过调用下面方法，退出当前登录的账号<br>
	 * 通过退出账号侦听器（此侦听器在初始化前经由setLogoutNotifyListener 方法设置）<br>
	 * 把退出消息返回给游戏，游戏可根据状态码进行相应的处理。<br>
	 */
	private void ucSdkLogout() {
		try {
			UCGameSDK.defaultSDK().logout();
		} catch (UCCallbackListenerNullException e) {
			// 未设置退出侦听器
		}
	}

	/**
	 * 必接功能<br>
	 * 悬浮按钮创建及显示<br>
	 * 悬浮按钮必须保证在SDK进行初始化成功之后再进行创建需要在UI线程中调用<br>
	 * 创建时出现java.io.IOException: Is a directory的警告日志属于正常情况，不影响调用<br>
	 */
	private void ucSdkFloatButton() {
		StartupActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				try {
					// 创建悬浮按钮。该悬浮按钮将悬浮显示在GameActivity页面上，点击时将会展开悬浮菜单，菜单中含有
					// SDK 一些功能的操作入口。
					// 创建完成后，并不自动显示，需要调用showFloatButton(Activity,
					// double, double, boolean)方法进行显示或隐藏。
					UCGameSDK.defaultSDK().createFloatButton(StartupActivity.this,
							new UCCallbackListener<String>() {

								@Override
								public void callback(int statuscode, String data) {
									Log.d("SelectServerActivity`floatButton Callback",
											"statusCode == " + statuscode
													+ "  data == " + data);
								}
							});
					// 显示悬浮图标，游戏可在某些场景选择隐藏此图标，避免影响游戏体验
					UCGameSDK.defaultSDK().showFloatButton(StartupActivity.this,
							100, 50, true);

				} catch (UCCallbackListenerNullException e) {
					e.printStackTrace();
				} catch (UCFloatButtonCreateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 选接功能<br>
	 * 九游社区功能<br>
	 * 如已在程序中调用悬浮按钮，则可不需再调用九游社区功能<br>
	 */
	private void ucSdkEnterUserCenter() {
		try {
			UCGameSDK.defaultSDK().enterUserCenter(getApplicationContext(),
					new UCCallbackListener<String>() {
						@Override
						public void callback(int statuscode, String data) {
							switch (statuscode) {
							case UCGameSDKStatusCode.SUCCESS:
								break;
							// !!! 未初始化成功。应该先调用初始化
							case UCGameSDKStatusCode.NO_INIT:
								ucSdkInit();
								break;
							// !!! 未登录。应该先调用登录功能进行登录
							case UCGameSDKStatusCode.NO_LOGIN:
								ucSdkLogin();
								break;
							default:
								break;
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 定义充值金额，默认为0
	private float amount = 0;

	/**
	 * 必接功能<br>
	 * SDK支付功能<br>
	 * 调用SDK支付功能 如你在调用支付页面时，没有显示正确的支付页面，请检查以下几点：<br>
	 * 1、是否已经提交对应环境的支付回调地址给技术接口人，是否配置到对应的环境中<br>
	 * 2、检查pInfo.setServerId()传入的值是否正确。<br>
	 * 在联调环境中进行支付，可使用无效卡进行支付，只需符合卡号卡密长度位数即可<br>
	 * 当卡号卡密全部输入为1时，是支付失败的订单，服务器将会收到订单状态为F的订单信息<br>
	 */
	private void ucSdkPay() {
		PaymentInfo pInfo = new PaymentInfo(); // 创建Payment对象，用于传递充值信息

		// 设置充值自定义参数，此参数不作任何处理，
		// 在充值完成后，sdk服务器通知游戏服务器充值结果时原封不动传给游戏服务器传值，字段为服务端回调的callbackInfo字段
		pInfo.setCustomInfo("callback");

		// 游戏服务器。此参数为可选参数，默认为空。此参数应使用UC分配的serverID进行传值。
		// 或当设置为0 时，会使用初始化时设置的服务器ID。
		// 如此值传入错误，会导致支付界面无法正常打开。
		pInfo.setServerId(UCSdkConfig.serverId);

		pInfo.setRoleId("102"); // 设置用户的游戏角色的ID，此为可选参数
		pInfo.setRoleName("游戏角色名"); // 设置用户的游戏角色名字，此为可选参数
		pInfo.setGrade("12"); // 设置用户的游戏角色等级，此为可选参数

		// 当传入一个amount作为金额值进行调用支付功能时，SDK会根据此amount可用的支付方式显示充值渠道
		// 如你传入6元，则不显示充值卡选项，因为市面上暂时没有6元的充值卡，建议使用可以显示充值卡方式的金额
		pInfo.setAmount(amount);// 设置充值金额，此为可选参数

		try {
			UCGameSDK.defaultSDK().pay(getApplicationContext(), pInfo,
					payResultListener);
		} catch (UCCallbackListenerNullException e) {
			// 异常处理
		}

	}

	private UCCallbackListener<OrderInfo> payResultListener = new UCCallbackListener<OrderInfo>() {
		@Override
		public void callback(int statudcode, OrderInfo orderInfo) {
			if (statudcode == UCGameSDKStatusCode.NO_INIT) {
				// 没有初始化就进行登录调用，需要游戏调用SDK初始化方法
			}
			if (statudcode == UCGameSDKStatusCode.SUCCESS) {
				// 成功充值
				if (orderInfo != null) {
					String ordereId = orderInfo.getOrderId();// 获取订单号
					float orderAmount = orderInfo.getOrderAmount();// 获取订单金额
					int payWay = orderInfo.getPayWay();
					String payWayName = orderInfo.getPayWayName();
					System.out.print(ordereId + "," + orderAmount + ","
							+ payWay + "," + payWayName);
				}
			}
			if (statudcode == UCGameSDKStatusCode.PAY_USER_EXIT) {
				// 用户退出充值界面。
			}
		}

	};

	/**
	 * 选接功能<br>
	 * 通知SDK用户进入游戏分区功能，将玩家实际进入的游戏分区名称、角色ID、角色名称传递给SDK。
	 */
	private void ucSdkNotifyZone() {
		// !!!以下参数值为模拟值，接入时需获取客户端实际内容进行传递
		UCGameSDK.defaultSDK().notifyZone("龙吟虎啸", "111", "傲红尘");
		Log.e("UCGameSDK", "通知SDK用户进入游戏分区功能调用成功");
	}

	
	/**
	 * 调用demo游戏登录页面
	 */
	private void dispalyGameLoginUI() {
		//this.setContentView(R.layout.login);
		// 执行SDK初始化功能
		UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);
		ucSdkInit();
//		Button gameLoginBtn = (Button) this.findViewById(R.id.gameLoginBtn);
//		gameLoginBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 调用SDK登录功能
//				ucSdkLogin();
//			}
//		});
	}


	/*
	 * 返回键方法
	 */
	public void onBackPressed() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("信息提示")
				.setMessage("您确定要退出游戏吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 调用退出SDK功能
						ucSdkExit();
						finish();
					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create();
		dialog.show();
	}

	@Override
	protected void onStart() {
		GLog.d("GameActivity", "----------onStart---------");
		if (gameView != null) {
			gameView.setRunning(true);
		}
		super.onStart();
	}

	@Override
	protected void onRestart() {
		GLog.d("GameActivity", "----------onRestart---------");
		if (gameView != null) {
			gameView.setRunning(true);
		}
		super.onRestart();
	}

	@Override
	protected void onResume() {
		GLog.d("GameActivity", "----------onResume---------");
		if (gameView != null) {
			gameView.setRunning(true);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		GLog.d("GameActivity", "----------onPause---------");
		if (gameView != null) {
			gameView.setRunning(false);
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		GLog.d("GameActivity", "----------onStop---------");
		if (gameView != null) {
			gameView.setRunning(false);
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		GLog.d("GameActivity", "----------onDestroy---------");
		try {
			if (gameView != null) {
				gameView.setRunning(false);
				Thread.sleep(16);
			}
			UCGameSDK.defaultSDK().destoryFloatButton(StartupActivity.this);
			super.onDestroy();
		} catch (Exception e) {
		}
	}
	
	
	public String MD5(String string) {
		byte[] hash;
		try {
		hash = MessageDigest.getInstance("MD5").digest(
		string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		return null;
		} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		return null;
		}


		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
		if ((b & 0xFF) < 0x10)
		hex.append("0");
		hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();// 32浣�	
	}
	
	private void confirmConnect(String uc_sid)
	{
		URL url = null;
		long time =System.currentTimeMillis() / 1000;
		String urlStr = "http://jjapi.appqj.com/api/CReturn/UcAndroidLogin?cid=603&uc_sid="+uc_sid+"&time="+Long.toString(time)+"&sign=";

		String signStr = "cid=603&uc_sid=" + uc_sid + "&time=" + Long.toString(time) + "&key=10498844eff809284cf8928bbd6ef508";
		String MD5Str = MD5(signStr);
		urlStr += MD5Str;
		
		System.out.println("connectionURLconnectionURLconnectionURL");
		System.out.println(urlStr);
		
		try {
			url = new URL(urlStr);
			System.out.println("connectionURLconnectionURLconnectionURL1111");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			System.out.println("connectionURLconnectionURLconnectionURL2222");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.setRequestMethod("GET");
			System.out.println("connectionURLconnectionURLconnectionURL4444");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader Buffreader = new BufferedReader(inputReader);
			String inputLine = null;
			String result ="";
			while(((inputLine = Buffreader.readLine()) != null))
			{
				result += inputLine;
			}
			
			System.out.println("result:" + result);
			Buffreader.close();
			connection.disconnect();
            
			int ucid = 0;
            try {
				JSONObject jsn = new JSONObject(result);
				JSONObject data = jsn.getJSONObject("data");
				String name = data.getString("nickname");
				ucid = data.getInt("ucid");
				System.out.println("nickname" + name);
				System.out.println("ucid:" + ucid);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			String userName = ucid +"";
			this.finish();
            GameUserData.setValue("userName", userName);
            Intent cc2dContext = new Intent(this, com.haowan123.kof.uc.GameBox.class);
            startActivity(cc2dContext);
            
			System.out.println("connectionURLconnectionURLconnectionURL3333");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}