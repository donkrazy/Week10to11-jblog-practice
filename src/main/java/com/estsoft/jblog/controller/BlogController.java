package com.estsoft.jblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.jblog.annotation.Auth;
import com.estsoft.jblog.service.BlogService;

@Controller
@RequestMapping( "/blog" )
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@Auth
	@RequestMapping( "/" )
	public String root() {
		return "/index";
	}
	
	@RequestMapping( "/{name}" )
	public String getBlog(@PathVariable("name") String name, Model model, @RequestParam(value = "cat", required=true, defaultValue="0") int cat) {
		//TODO: 블로그를 만들지 않은 유저의 이름으로 접근할 때 처리: admin으로 연결
		if(cat==0){
			blogService.getBlogByName(name, model);
			return "blog/main";
		}
		
		//TODO: 포스트가 존재하지 않는 카테고리일 때 처리
		//TODO: 또라이 유저들이 cat에 이상한 입력할때. 근데 string넣으면 400에러뜬다. 400에러페이지 만들기
		boolean isPostExists = blogService.getBlogByCategoryId(name, model, cat);
		if(!isPostExists){
			return "blog/error";
		}
		return "blog/main";
	}

	
	//TODO: name과 post_id는 종속. 글쓴이에 해당하지 않는 요청 처리
	@RequestMapping( "/{name}/{post_id}" )
	public String getBlogByPostId(@PathVariable("name") String name, Model model, @PathVariable("post_id") int post_id) {
		blogService.getBlogByPostId(name, model, post_id);
		return "blog/main";
	}
	
	@Auth
	@RequestMapping( "/admin" )
	public String admin() {
		return "blog/admin";
	}
	
	@Auth
	@RequestMapping( "/category" )
	public String admin2() {
		return "blog/category";
	}
	
	@Auth
	@RequestMapping( "/write" )
	public String admin3() {
		return "blog/write";
	}
}