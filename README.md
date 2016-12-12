# ProxyCrawl
从快代理网站获取代理信息，并验证是否可用
  程序主入口为GetAgentTask.java的main方法
 * 1.请求快代理网站免费代理页面，获取总页面个数pages  
 * 2.循环pages，请求每个页面，获取页面table中代理信息，实例化为Proxyhost对象 
 * 3.验证代理是否可用
