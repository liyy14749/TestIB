package com.stock.core.util.receiptReq;

import com.stock.core.exception.BusinessException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;

public class IOSReceiptUtil {
	
	private static final Logger log = LoggerFactory.getLogger(IOSReceiptUtil.class);

	public static String IOS_SUBSCRIBE_PWD = "26436429744246ca80f0361d5dd6b325";
	
//	public static IOSResult validateReceipt(String receipt) throws BusinessException{
//		HttpRequest request = HttpRequest.post("https://buy.itunes.apple.com/verifyReceipt");
//		JSONObject json=new JSONObject();
//		json.put("receipt-data", receipt);
//		json.put("password", IOS_SUBSCRIBE_PWD);
//		request.header("Content-Type", "application/json");
//		request.send(json.toJSONString());
//		int code = request.code();
//		if(code==200||code==201){
//			String resp = request.body();
//			//System.out.println(resp);
//			JSONObject obj = JSON.parseObject(resp);
//			if(obj!=null&&obj.getInteger("status")!=null&&obj.getInteger("status")==21007){
//				request = HttpRequest.post("https://sandbox.itunes.apple.com/verifyReceipt");
//				json=new JSONObject();
//				json.put("receipt-data", receipt);
//				json.put("password", IOS_SUBSCRIBE_PWD);
//				request.header("Content-Type", "application/json");
//				request.send(json.toJSONString());
//				code = request.code();
//				if(code==200||code==201){
//					resp = request.body();
//					//System.out.println(resp);
//					obj = JSON.parseObject(resp);
//					if(obj!=null&&obj.getInteger("status")!=null&&(obj.getInteger("status")==0||obj.getInteger("status")==21006)){
//						try {
//							if(resp.indexOf("transaction_id")>-1){
//								return JSON.parseObject(resp,IOSResult.class);
//							}
//						} catch (Exception e) {
//						}
//					} else {
//						System.out.println(resp);
//						throw new BusinessException(1014, "票据错误，校验错误,"+obj.getInteger("status"));
//					}
//				} else {
//					throw new BusinessException(1014, "票据错误，网络错误");
//				}
//			} else if(obj!=null&&obj.getInteger("status")!=null&&(obj.getInteger("status")==0||obj.getInteger("status")==21006)){
//				try {
//					if(resp.indexOf("transaction_id")>-1){
//						return JSON.parseObject(resp,IOSResult.class);
//					}
//				} catch (Exception e) {
//				}
//			} else {
//				System.out.println(resp);
//				throw new BusinessException(1014, "票据错误，校验错误,"+obj.getInteger("status"));
//			}
//		} else {
//			throw new BusinessException(1014, "票据错误，网络错误");
//		}
//		return null;
//	}
	
	public static IOSResult validateReceiptNotException(String receipt,String iosSubscribePwd) {
		try {
			HttpRequest request = HttpRequest.post("https://buy.itunes.apple.com/verifyReceipt");
			JSONObject json=new JSONObject();
			json.put("receipt-data", receipt);
			json.put("password", iosSubscribePwd);
			request.header("Content-Type", "application/json");
			request.send(json.toJSONString());
			int code = request.code();
			if(code==200||code==201){
				String resp = request.body();
				//System.out.println(resp);
				JSONObject obj = JSON.parseObject(resp);
				if(obj!=null&&obj.getInteger("status")!=null&&obj.getInteger("status")==21007){
					request = HttpRequest.post("https://sandbox.itunes.apple.com/verifyReceipt");
					json=new JSONObject();
					json.put("receipt-data", receipt);
					json.put("password", iosSubscribePwd);
					request.header("Content-Type", "application/json");
					request.send(json.toJSONString());
					code = request.code();
					if(code==200||code==201){
						resp = request.body();
						//System.out.println(resp);
						obj = JSON.parseObject(resp);
						if(obj!=null&&obj.getInteger("status")!=null&&(obj.getInteger("status")==0||obj.getInteger("status")==21006)){
							try {
								if(resp.indexOf("transaction_id")>-1){
									return JSON.parseObject(resp,IOSResult.class);
								}
							} catch (Exception e) {
								log.error("parseObject error",e);
							}
						} else {
							System.out.println(resp);
							throw new BusinessException(1014, "票据错误，校验错误,"+obj.getInteger("status"));
						}
					} else {
						throw new BusinessException(1014, "票据错误，网络错误");
					}
				} else if(obj!=null&&obj.getInteger("status")!=null&&(obj.getInteger("status")==0||obj.getInteger("status")==21006)){
					try {
						if(resp.indexOf("transaction_id")>-1){
							return JSON.parseObject(resp,IOSResult.class);
						}
					} catch (Exception e) {
					}
				} else {
					System.out.println(resp);
					throw new BusinessException(1014, "票据错误，校验错误,"+obj.getInteger("status"));
				}
			} else {
				throw new BusinessException(1014, "票据错误，网络错误");
			}
			return null;
		} catch (Exception e) {
			log.error("validateReceiptNotException error",e);
			return null;
		}
	}
	
	public static void main(String[] args) {
		try {
//			String receipt=IOUtils.toString(new FileInputStream("F:/soft/workspace/war-heroes/game-proxy/src/test/java/receipt.txt"),"utf-8");
			String receipt=IOUtils.toString(IOSReceiptUtil.class.getResourceAsStream("/receipt.txt"),"utf-8");
//			System.out.println(JSON.toJSONString(validateReceiptNotException(receipt)));
			IOSResult r = IOSReceiptUtil.validateReceiptNotException(receipt,IOS_SUBSCRIBE_PWD);
			//List<com.subscribe.core.util.receiptReq.LatestReceiptInfo> latestList = r.getLatest_receipt_info();
//			List<com.subscribe.core.util.receiptReq.LatestReceiptInfo> latestList = new ArrayList<LatestReceiptInfo>();
//			LatestReceiptInfo a = new LatestReceiptInfo();
//			a.setExpires_date_ms(2L);
//			latestList.add(a);
//			LatestReceiptInfo b = new LatestReceiptInfo();
//			b.setExpires_date_ms(3L);
//			latestList.add(b);
//			Collections.sort(latestList);
//			System.out.println(Arrays.toString(latestList.toArray()));
			//System.out.println(JSON.toJSONString(validateReceipt(receipt).getReceipt().getIn_app()));
			//System.out.println(validateReceipt(receipt).getReceipt().getIn_app().get(0).getProduct_id());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
