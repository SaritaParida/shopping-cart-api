package org.jsp.api.service;

import org.jsp.api.dao.MerchantDao;
import org.jsp.api.dao.UserDao;
import org.jsp.api.dto.Merchant;
import org.jsp.api.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Service
public class GenerateLinkService {
	@Autowired
	private MerchantDao mdao;
	@Autowired
	private UserDao udao;

	public String getVerificationLink(HttpServletRequest request, Merchant merchant) {
		String token = RandomString.make(45);
		merchant.setToken(token);
		merchant.setStatus("InActive");
		mdao.updateMerchant(merchant);
		String siteurl = request.getRequestURL().toString();
		System.out.println(siteurl);
		String url = siteurl.replace(request.getServletPath(), "");
		System.out.println(url);
		String verify_link = url + "/merchants/verify?token=" + token;
		System.out.println(verify_link);
		return url +verify_link +token;
	}

	public String getResetPasswordLink(HttpServletRequest request, Merchant merchant) {
		String token = RandomString.make(45);
		merchant.setToken(token);
		mdao.updateMerchant(merchant);
		String siteurl = request.getRequestURL().toString();
		String url = siteurl.replace(request.getServletPath(), "");
		String reset_link = url + "/merchants/reset-password?token=" + token;
		return url+reset_link+token;
	}

	public String getVerificationLink(HttpServletRequest request, User user) {
		String token = RandomString.make(45);
		user.setToken(token);
		user.setStatus("InActive");
		udao.updateUser(user);
		String siteurl = request.getRequestURL().toString();
		System.out.println(siteurl);
		String url = siteurl.replace(request.getServletPath(), "");
		System.out.println(url);
		String verify_link = url + "/users/verify?token=" + token;
		System.out.println(verify_link);
		return url+verify_link+token;
	}

	public String getResetPasswordLink(HttpServletRequest request, User user) {
		String token = RandomString.make(45);
		user.setToken(token);
		udao.updateUser(user);
		String siteurl = request.getRequestURL().toString();
		String url = siteurl.replace(request.getServletPath(), "");
		String reset_link = url + "/users/reset-password?token=" + token;
		return url+reset_link+token;
	}

}
