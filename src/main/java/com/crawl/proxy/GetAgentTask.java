package com.crawl.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawl.entity.Proxyhost;

/**
 * 程序主入口
 * 1.请求快代理网站免费代理页面，获取总页面个数pages
 * 2.循环pages，请求每个页面，获取页面table中代理信息，实例化为Proxyhost对象
 * 3.验证代理是否可用
 * @author Administrator
 *
 */
public class GetAgentTask extends TimerTask {
	private static String kuaidailiBaseUrl = "http://www.kuaidaili.com/free/inha/"; // 可获取列表信息

	@Override
	public void run() {
		AgentMemoryDB.removePctemp();
		goKuaidailiBaseUrl();
		// 验证可用性
		AgentMemoryDB.Check();
	}

	// 获取代理信息
	private static void goKuaidailiBaseUrl() {

		//获取总共有多少页代理（例如：1879）
		int pages = getPages(httpGet(kuaidailiBaseUrl + "1/"));
		if (pages > 0) {
			for (int i = 1; i <= pages; i++) {
				try {
					//线程睡眠至少1秒钟，否则网站服务器请求503错误
					Thread.sleep(1000);
					System.out.println(kuaidailiBaseUrl + i + "/");
					Document document = httpGet(kuaidailiBaseUrl + i + "/");
					//将每一页中每一个tr的信息提取出来，实例化为一个Proxyhost对象，放到list集合中
					List<Proxyhost> list = getProxyhostByDoc(document);
					if (list.size() > 0) {
						// 存放到代理库 k
						List<Proxyhost> mapList = AgentMemoryDB.getListForMaoByKey("k");
						if (mapList == null) {
							mapList = new ArrayList<Proxyhost>();
						}
						mapList.addAll(list);
						//将每一页的list集合储存到map集合中，为下一步验证代理是否可用时使用
						AgentMemoryDB.addOrUpdateMap("k", mapList);
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static Document httpGet(String url) {
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return document;
	}

	// 获取页数
	static int getPages(Document document) {
		Element elements = document.select("#listnav").select("a").last();
		String html = elements.html().trim();
		return Integer.parseInt(html);
	}

	// 获取代理列表
	static List<Proxyhost> getProxyhostByDoc(Document document) {
		List<Proxyhost> list = new ArrayList<Proxyhost>();
		Elements elements = document.select(".table-striped").select("tbody");
		Elements trs = elements.select("tr");
		for (int i = 0; i < trs.size(); i++) {
			Element tr = trs.get(i);
			if (tr != null) {
				Elements tds = tr.select("td");
				Proxyhost p = new Proxyhost();
				if (tds.get(2).text().trim().indexOf("匿名") > -1) {
					p.setHostIp(tds.get(0).text().trim());
					p.setHostPort(Integer.parseInt(tds.get(1).text().trim()));
					list.add(p);
				}
			}

		}
		return list;
	}

	public static void main(String[] args) {
		AgentMemoryDB.removePctemp();
		goKuaidailiBaseUrl();
		// 验证可用性
		AgentMemoryDB.Check();
	}
}
