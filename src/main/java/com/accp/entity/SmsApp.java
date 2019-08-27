package com.accp.entity;

import com.accp.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("短信应用")
@TableName("bw_sms_app")
public class SmsApp extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("用户ID")
	private Long userId;
	@ApiModelProperty("应用ID")
	private String appId;
	@ApiModelProperty("应用名称")
	private String appName;
	@ApiModelProperty("应用联系人")
	private String contactName;
	@ApiModelProperty("联系电话")
	private String mobileNo;
	@ApiModelProperty("私钥")
	private String privateKey;
	@ApiModelProperty("公钥")
	private String publicKey;
	@ApiModelProperty("备注/说明")
	private String remark;

}
