package com.crawl.proxy;

import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.crawl.entity.Proxyhost;

public class AgentCheckRunnable implements Runnable {
	private List<Proxyhost> list=null;
	private String key="";
	static HttpClient  client=null;
	
	public AgentCheckRunnable() {
	}
	public AgentCheckRunnable(List<Proxyhost> pList,String key) {
		this.list=pList;
		this.key=key;
	}
	
	public void run() {
		if (list==null || list.size()==0) {
			return ;
		}else {
			System.out.println("【"+key+"】验证总数："+list.size());
			for (int i = 0; i < list.size(); i++) {
				if (!isAvailable(list.get(i))) {
					list.remove(i);
					i--;
				}
			}
			System.out.println("【"+key+"】可用总数："+list.size());
			AgentMemoryDB.addOrUpdateMap(key, list);
		}
		
	}
	
	  boolean isAvailable(Proxyhost p){
		String testUrl="";
		 if (client==null) {
			client= new HttpClient();
			client.getParams().setAuthenticationPreemptive(true); 
			client.getParams().setParameter("http.socket.timeout",1000*1);
			client.getParams().setParameter("http.connection.timeout",1000*1);
			client.getParams().setParameter("http.connection-manager.timeout",100000000L); 
		}
		client.getHostConfiguration().setProxy(p.getHostIp(), p.getHostPort());   
		
		testUrl=AgentMemoryDB.CHICK_IP_URL[new Random().nextInt(5)];
		GetMethod getMethod=new GetMethod(testUrl);
     
     try {
		int i=client.executeMethod(getMethod);
		System.out.println("验证结果："+i);
		if (i==200 || i==403 || i==404 || i==405) {
			return true;
		}
	} catch (Exception e1) {
		System.out.println("验证代理超时/异常信息------------>isAvailable[ "+p.getHostIp()+":"+p.getHostPort()+":"+testUrl+" ]");
		return false;
	}
		
		return false;
	};

}
