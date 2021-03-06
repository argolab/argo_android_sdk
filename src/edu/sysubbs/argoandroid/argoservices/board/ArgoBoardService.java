package edu.sysubbs.argoandroid.argoservices.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import edu.sysubbs.argoandroid.argoobject.ArgoBoard;
import edu.sysubbs.argoandroid.argoobject.ArgoSectionBoards;
import edu.sysubbs.argoandroid.util.ErrorException;
import edu.sysubbs.argoandroid.util.HttpManager;
import edu.sysubbs.argoandroid.util.Site;

public class ArgoBoardService {
	public ArrayList<String> getAllBoardnameList(String cookie) throws ErrorException {
		HttpManager httpManager = new HttpManager();
		try {
			HttpURLConnection connection = httpManager.baseConnect(Site.GET_ALL_BORAD_NAME, cookie, "GET");
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			
			String result = reader.readLine();
			JSONObject object = new JSONObject(result);
			if (object.get("success").equals("1")) {
				JSONArray jsonArray = object.getJSONArray("data");
				ArrayList<String> boardnameList = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					boardnameList.add(jsonArray.get(i).toString());
				}
				return boardnameList;
			}
			else {
				String error = object.get("error").toString();
				String code = object.get("code").toString();
				throw new ErrorException(error, code);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<ArgoSectionBoards> getAllBoardInfo() throws ErrorException {
		HttpManager httpManager = new HttpManager();
		try {
			HttpURLConnection connection = httpManager.baseConnect(Site.GET_ALL_BOARD_INFO, null, "GET");
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			String value = "";
			String readValue = "";
			while ((readValue = reader.readLine()) != null) {
				value += readValue;
			}
			JSONObject object = new JSONObject(value);
			if (object.get("success").toString().equals("1")) {
				JSONArray array = object.getJSONObject("data").getJSONArray("all");
				ArrayList<ArgoSectionBoards> boardInfoList = new ArrayList<ArgoSectionBoards>();
				for (int i = 0; i < array.length(); i++) {
					ArgoSectionBoards board = new ArgoSectionBoards();
					board.parse(array.getJSONObject(i));
					boardInfoList.add(board);
				}
				return boardInfoList;
			}
			else {
				String error = object.get("error").toString();
				String code = object.get("code").toString();
				throw new ErrorException(error, code);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArgoBoard getBoardInfo(String boardname) throws ErrorException {
		HttpManager manager = new HttpManager();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("boardname", boardname);
		ArgoBoard board = manager.getResponseAsObject(Site.GET_BOARD_INFO, null, data, ArgoBoard.class);
		
		return board;
	}
	
	public ArrayList<ArgoBoard> getBoardsInfoBySecCode(String secCode) throws ErrorException {
		HttpManager manager = new HttpManager();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("sec_code", secCode);
		ArrayList<ArgoBoard> boardList = manager.getResponseAsList(Site.GET_BOARD_INFO_BY_SECCODE, null, data, ArgoBoard.class);
		
		return boardList;
	}
	
	public boolean cleanBoardUnread(String cookie, String boardname) {
		HttpManager manager = new HttpManager();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("boardname", boardname);
		
		boolean success = false;
		try {
			manager.postDataByMapAndGetBaseObject(Site.CLEAR_BOARD_UNREAD, cookie, data);
			success = true;
		} catch (ErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	public ArrayList<String> getReadedPostIndexList(String cookie, String boardname) throws ErrorException {
		HttpManager manager = new HttpManager();	
		try {
			HttpURLConnection connection = manager.baseConnect(Site.GET_READED_POST_INDEX_LIST, cookie, "GET");
			JSONObject data = new JSONObject();
			data.put("boardname", boardname);
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			writer.print(data);
			writer.flush();
			connection.connect();
	
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			
			String result = reader.readLine();
			JSONObject object = new JSONObject(result);
			if (object.get("success").equals("1")) {
				JSONArray jsonArray = object.getJSONArray("data");
				ArrayList<String> readedPostIndexList = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					readedPostIndexList.add(jsonArray.get(i).toString());
				}
				return readedPostIndexList;
			}
			else {
				String error = object.get("error").toString();
				String code = object.get("code").toString();
				throw new ErrorException(error, code);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
