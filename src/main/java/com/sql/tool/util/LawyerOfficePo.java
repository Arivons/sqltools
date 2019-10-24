package com.sql.tool.util;

import lombok.Data;
import java.io.Serializable;

/**
 *
 * @author liyupeng
 * @Date: 2019-10-24 19:15:15.813
 */
@Data
public class LawyerOfficePo implements Serializable {


	/**
	 * 数据库主键，自增长
	 */
  private Long id;

	/**
	 * 律师事务所编码
	 */
  private Long lawyerOfficeCode;

	/**
	 * 所在省份编码
	 */
  private Long provinceCode;

	/**
	 * 所在城市编码
	 */
  private Long cityCode;

	/**
	 * 名称
	 */
  private String name;

	/**
	 * 地址
	 */
  private String address;

	/**
	 * 联系电话
	 */
  private String tel;

	/**
	 * 经纬度
	 */
  private String longitudeLatitude;

	/**
	 * 是否删除，0-正常，1-删除
	 */
  private Long deleted;

	/**
	 * 创建人id
	 */
  private Long creator;

	/**
	 * 创建时间
	 */
  private java.util.Date createTime;

	/**
	 * 修改者id
	 */
  private Long updator;

	/**
	 * 修改时间
	 */
  private java.util.Date updateTime;

	/**
	 * 备注
	 */
  private String remark;

	public LawyerOfficePo() {
	}
}
