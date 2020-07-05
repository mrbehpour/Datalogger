package mycomponents;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.saa.android.datalogger.R;

public class MyUtilities {

	public static Boolean isNumber(String str){
		Boolean res = false;
		Pattern p = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$");
		Matcher m = p.matcher(str);  
		res = m.matches();  
		return res;
	}

	public static String getValidDigit(String strDigit){
		String res = "";
		if(strDigit != null && strDigit.trim().length()>0 && MyUtilities.isNumber(strDigit)){
			if(strDigit.contains(".")){
				strDigit = strDigit.replace(".", ",");
				if(strDigit.trim().split(",")[1].length()>2){
					if(Integer.valueOf(strDigit.trim().split(",")[1].substring(0, 2))==0){
						res = strDigit.trim().split(",")[0].trim();
					}else{
						DecimalFormat decimalforamt = new DecimalFormat("#0.00");
						res = decimalforamt.format(Double.valueOf(strDigit.replace(",", ".").trim()));
					}
				}else{
					if(Integer.valueOf(strDigit.trim().split(",")[1])==0){
						res = strDigit.trim().split(",")[0].trim();
					}else{
						DecimalFormat decimalforamt = new DecimalFormat("#0.00");
						res = decimalforamt.format(Double.valueOf(strDigit.replace(",", ".").trim()));
					}
				}
			}else{
				res = strDigit.trim();
			}
		}
		return res;
	}

	public static String getStringContentFromFile(Context context,String strFilePathInAsset)  {
		StringBuilder total = new StringBuilder();
		try {
			InputStream inputStream = context.getAssets().open(strFilePathInAsset);
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line).append('\n');
			}
		}catch (Exception ex){
			Log.i("TAG",ex.getMessage());
		}
		return total.toString();
	}
}
