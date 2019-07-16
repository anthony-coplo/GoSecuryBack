package com.compareAPI.compare;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;
import javax.xml.bind.DatatypeConverter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Entity.Image;
import Entity.Ressources;
import com.compareAPI.compare.connect; 

@RestController
@RequestMapping("/upload")
@CrossOrigin(value = "*")
public class Compare {
	
	@PostMapping()
	public Double comparision (@RequestBody ImageContent imgString) throws Exception {
	 
    //File file1 = new File("C:\\Users\\anthony\\Desktop\\face2.jpg");
	
	connect connect = new connect();	
	connect.Connection();
	System.out.println("image content => " + imgString.getImgContent());
	double value = 0;
	
	
	Collection<Image> user = connect.getUsers();
	byte[] buff = DatatypeConverter.parseBase64Binary(imgString.getImgContent());
		
	for(Image u: user) {
		
		byte[] buff1 = DatatypeConverter.parseBase64Binary(u.getUrl());
	
		// face++ compare API url
		String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
		
	    HashMap<String, String> map = new HashMap<>();
	    HashMap<String, byte[]> byteMap = new HashMap<>();
	    
	    map.put("api_key", "MPFDHN_l4uW8i7u8LK-fBK3AjX4dHrmj");
	    map.put("api_secret", "9BDIDZ9N52W0gCctSrVH8qEcDYtAlQYF");
	    byteMap.put("image_file1", buff);
	    byteMap.put("image_file2", buff1);
	
	    try {
	        byte[] bacd = post(url, map, byteMap);
	        String str = new String(bacd);
	        String[] tab = str.split(",");
	        for (String  s : tab) {
	        	System.out.println(s);
	        	if(s.contains("confidence")) {
	        		// return confidence score if > 80 percents
	        		if(Double.parseDouble(s.split(":")[1]) > value) {
	        			value = Double.parseDouble(s.split(":")[1]);
	        			if(value > 80) return new Double(1);
	        		} 
	        	}
	        }
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
		}
	    
	}
	return null;
	
}

private final static int CONNECT_TIME_OUT = 30000;
private final static int READ_OUT_TIME = 50000;
private static String boundaryString = getBoundary();

protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
    HttpURLConnection conne;
    URL url1 = new URL(url);
    conne = (HttpURLConnection) url1.openConnection();
    conne.setDoOutput(true);
    conne.setUseCaches(false);
    conne.setRequestMethod("POST");
    conne.setConnectTimeout(CONNECT_TIME_OUT);
    conne.setReadTimeout(READ_OUT_TIME);
    conne.setRequestProperty("accept", "*/*");
    conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
    conne.setRequestProperty("connection", "Keep-Alive");
    conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
    DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
    Iterator iter = map.entrySet().iterator();
    while(iter.hasNext()){
        Map.Entry<String, String> entry = (Map.Entry) iter.next();
        String key = entry.getKey();
        String value = entry.getValue();
        obos.writeBytes("--" + boundaryString + "\r\n");
        obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                + "\"\r\n");
        obos.writeBytes("\r\n");
        obos.writeBytes(value + "\r\n");
    }
    if(fileMap != null && fileMap.size() > 0){
        Iterator<?> fileIter = fileMap.entrySet().iterator();
        while(fileIter.hasNext()){
            Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                    + "\"; filename=\"" + encode(" ") + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.write(fileEntry.getValue());
            obos.writeBytes("\r\n");
        }
    }
    
    obos.writeBytes("--" + boundaryString + "--" + "\r\n");
    obos.writeBytes("\r\n");
    obos.flush();
    obos.close();
    
    InputStream ins = null;
    
    int code = conne.getResponseCode();
    
    try{
        if(code == 200){
            ins = conne.getInputStream();
        }else{
            ins = conne.getErrorStream();
        }
    }catch (SSLException e){
        e.printStackTrace();
        return new byte[0];
    }
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buff = new byte[4096];
    int len;
    while((len = ins.read(buff)) != -1){
        baos.write(buff, 0, len);
    }
    
    byte[] bytes = baos.toByteArray();
    ins.close();
    return bytes;
}

	private static String getBoundary() {
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for(int i = 0; i < 32; ++i) {
	        sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
	    }
	    
	    return sb.toString();
	}
	
	
	private static String encode(String value) throws Exception{
	    return URLEncoder.encode(value, "UTF-8");
	}
	
	
	public static byte[] getBytesFromFile(File f) {
	    if (f == null) {
	        return null;
	    }
	    
	    try {
	        FileInputStream stream = new FileInputStream(f);
	        ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
	        byte[] b = new byte[1000];
	        int n;
	        while ((n = stream.read(b)) != -1)
	            out.write(b, 0, n);
	        stream.close();
	        out.close();
	        return out.toByteArray();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    return null;
	}

}
