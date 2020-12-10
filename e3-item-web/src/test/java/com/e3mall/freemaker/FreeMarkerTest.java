package com.e3mall.freemaker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTest {

	
	@Test
	public void gen() throws Exception {
		//创建模板文件
		//configuration: 模板文件保存目录，模板文件编码格式
		Configuration configuration = new Configuration(Configuration.getVersion());
		//加载模板，创建模板对象
		configuration.setDirectoryForTemplateLoading(new File("D:\\liang.li\\workSpace\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		configuration.setDefaultEncoding("UTF-8");
		Template template = configuration.getTemplate("student.ftl");
		//数据集：pojo、map
		Map<String, Object> map = new HashMap<>();
		Student stu = null;
		List<Student> list = new ArrayList<>();
		for(int i=1;i<=10;i++) {
			stu = new Student("名字"+i, 20+i, "喜"+i+"郎大厦",new Date());
			list.add(stu);
		}
		
		map.put("stuList", list);
		//创建writer对象：输出路径+文件名
		Writer writer = new FileWriter(new File("C:\\Users\\hp\\Desktop\\stu.html"));
		template.process(map, writer);
		//关闭流
		writer.close();
	}
}
