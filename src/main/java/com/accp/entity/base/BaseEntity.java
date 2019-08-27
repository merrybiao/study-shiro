package com.accp.entity.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * 实体Bean基类
 * 
 * @author Luyuan
 *
 */
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	protected Long id;

	protected LocalDateTime createTime;

	protected LocalDateTime updateTime;

}
