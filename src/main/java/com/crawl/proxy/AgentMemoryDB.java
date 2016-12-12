package com.crawl.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.crawl.entity.Proxyhost;

public class AgentMemoryDB {
	
	//验证代理IP 是否可用及响应速度
	public static final String[] CHICK_IP_URL={"http://proxies.site-digger.com/proxy-detect/",
												"http://www.baidu.com/img/baidu_jgylogo3.gif",
												"http://img4.cache.netease.com/www/v2013/img/nav_sprite_ie6_v4.png",
												"http://mat1.gtimg.com/www/images/qq2012/sogouSearchLogo20140629.png",
												"http://img02.taobaocdn.com/tps/i2/T1hOy5XhNnXXXXXXXX-9-14.gif"};

	//可以使用的代理列表
	public static List<Proxyhost> PC=null;
	
	//待验证的代理列表
	private static List<Proxyhost> pctemp=new ArrayList<Proxyhost>();
	
	//待验证代理仓库
	private static Map<String, List<Proxyhost> > pMap=new HashMap<String, List<Proxyhost>>();
	
	
	
	//更新m
	public static void addOrUpdateMap(String key,List<Proxyhost> list) {
		synchronized (pMap) {
			pMap.put(key, list);
		}
	}
	
	//获取子
	public static List<Proxyhost> getListForMaoByKey(String key){
		List<Proxyhost> list=pMap.get(key);
		return list;
	}

	public static void addProxyhostList(List<Proxyhost> list) {
		synchronized (pctemp) {
			pctemp.addAll(list);	
		}
	}
	
	public static List<Proxyhost> getPctemp() {
		return pctemp;
	}

	public static void removePctemp() {
		AgentMemoryDB.pctemp.clear();
	}

	//开始验证
	public static void Check(){
		int poolLength=pMap.keySet().size();
		ExecutorService pool = Executors.newFixedThreadPool(poolLength);
		if (poolLength>0) {//开启线程池进行验证
			Iterator<String> its=pMap.keySet().iterator();
			while (its.hasNext()) {
				String key=its.next();
				List<Proxyhost> taskList=pMap.get(key);
				pool.execute(new AgentCheckRunnable(taskList, key));
			}
			pool.shutdown();
			//等待所有线程完毕后 合并可用代理
			while (true) {				
				if (pool.isTerminated()) {
					Iterator<String> hb=pMap.keySet().iterator();
					while (hb.hasNext()) {
						String temp=hb.next();
						List<Proxyhost> newList=pMap.get(temp);
						pctemp.clear();
						pctemp.addAll(newList);
						PC=pctemp;
					}
					System.out.println("可用代理:"+pctemp.size());
					for (Proxyhost z : pctemp) {
						System.out.println(z.getHostIp()+":"+z.getHostPort());
					}
					break;
				}
			}
			
		}
		
		
	}
	
}
