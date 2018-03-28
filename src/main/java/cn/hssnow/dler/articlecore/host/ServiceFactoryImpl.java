package cn.hssnow.dler.articlecore.host;

import cn.hssnow.dler.articlecore.host.support.Host;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ServiceFactoryImpl implements ServiceFactory<BaseService> {
	private static final List<Class<? extends BaseService>> SERVICES = new ArrayList<>();

	static {
		Reflections reflections = new Reflections("cn.hssnow.dler.articlecore.host.impl");
		SERVICES.addAll(reflections.getSubTypesOf(BaseService.class));
	}
	
	private BaseService newInstance(String url) {
		if (url == null || "".equals(url.trim())) return null;
		for (Class<? extends BaseService> c : SERVICES) {
			if (url.contains(c.getAnnotation(Host.class).host())) {
				try {
					return c.newInstance();
				} catch (Exception e) {
					return null;
				}
			}
		}
		return null;
	}
	
	@Override
	public BaseService judge(String url) {
		BaseService service = newInstance(url);
		
		service.build(url);
		
		return service;
	}

}