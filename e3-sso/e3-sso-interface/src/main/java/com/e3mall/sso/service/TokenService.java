package com.e3mall.sso.service;

import com.e3mall.common.utils.E3Result;

public interface TokenService {

	E3Result getUserByToken(String token);
}
