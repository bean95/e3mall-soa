package com.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.e3mall.common.utils.FastDFSClient;
import com.e3mall.common.utils.JsonUtils;

@Controller
@RequestMapping("/pic")
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value="/upload", produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String upload(MultipartFile uploadFile) {
		try {
			FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
			String originName = uploadFile.getOriginalFilename();
			String extName = originName.substring(originName.lastIndexOf(".") + 1);
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			System.out.println(url);
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败2020");
			System.out.println("upload failed.");
			return JsonUtils.objectToJson(result);
		}
	}
	
}
