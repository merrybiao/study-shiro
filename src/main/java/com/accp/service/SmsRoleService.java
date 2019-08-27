package com.accp.service;

import java.util.List;

import com.accp.dto.SmsRoleDto;

public interface SmsRoleService {

	List<SmsRoleDto> selectRoleList();

	SmsRoleDto selectRoleById(Long roleId);

}
