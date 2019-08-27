package com.accp.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.accp.dto.SmsMenuDto;

/**
 * 权限数据处理
 * 
 * @author Luyuan
 */
public class TreeUtils {
	/**
	 * 根据父节点的ID获取所有子节点
	 * 
	 * @param list     分类表
	 * @param parentId 传入的父节点ID
	 * @return String
	 */
	public static List<SmsMenuDto> getChildPerms(List<SmsMenuDto> list, int parentId) {
		List<SmsMenuDto> returnList = new ArrayList<SmsMenuDto>();
		for (Iterator<SmsMenuDto> iterator = list.iterator(); iterator.hasNext();) {
			SmsMenuDto t = (SmsMenuDto) iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (t.getParentId() == parentId) {
				recursionFn(list, t);
				returnList.add(t);
			}
		}
		return returnList;
	}

	/**
	 * 递归列表
	 * 
	 * @param list
	 * @param t
	 */
	private static void recursionFn(List<SmsMenuDto> list, SmsMenuDto t) {
		// 得到子节点列表
		List<SmsMenuDto> childList = getChildList(list, t);
		t.setChildren(childList);
		for (SmsMenuDto tChild : childList) {
			if (hasChild(list, tChild)) {
				// 判断是否有子节点
				Iterator<SmsMenuDto> it = childList.iterator();
				while (it.hasNext()) {
					SmsMenuDto n = (SmsMenuDto) it.next();
					recursionFn(list, n);
				}
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private static List<SmsMenuDto> getChildList(List<SmsMenuDto> list, SmsMenuDto t) {

		List<SmsMenuDto> tlist = new ArrayList<SmsMenuDto>();
		Iterator<SmsMenuDto> it = list.iterator();
		while (it.hasNext()) {
			SmsMenuDto n = (SmsMenuDto) it.next();
			if (n.getParentId().longValue() == t.getMenuId().longValue()) {
				tlist.add(n);
			}
		}
		return tlist;
	}

	List<SmsMenuDto> returnList = new ArrayList<SmsMenuDto>();

	/**
	 * 根据父节点的ID获取所有子节点
	 * 
	 * @param list   分类表
	 * @param typeId 传入的父节点ID
	 * @param prefix 子节点前缀
	 */
	public List<SmsMenuDto> getChildPerms(List<SmsMenuDto> list, int typeId, String prefix) {
		if (list == null) {
			return null;
		}
		for (Iterator<SmsMenuDto> iterator = list.iterator(); iterator.hasNext();) {
			SmsMenuDto node = (SmsMenuDto) iterator.next();
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (node.getParentId() == typeId) {
				recursionFn(list, node, prefix);
			}
			// 二、遍历所有的父节点下的所有子节点
			/*
			 * if (node.getParentId()==0) { recursionFn(list, node); }
			 */
		}
		return returnList;
	}

	private void recursionFn(List<SmsMenuDto> list, SmsMenuDto node, String p) {
		// 得到子节点列表
		List<SmsMenuDto> childList = getChildList(list, node);
		if (hasChild(list, node)) {
			// 判断是否有子节点
			returnList.add(node);
			Iterator<SmsMenuDto> it = childList.iterator();
			while (it.hasNext()) {
				SmsMenuDto n = (SmsMenuDto) it.next();
				n.setMenuName(p + n.getMenuName());
				recursionFn(list, n, p + p);
			}
		} else {
			returnList.add(node);
		}
	}

	/**
	 * 判断是否有子节点
	 */
	private static boolean hasChild(List<SmsMenuDto> list, SmsMenuDto t) {
		return getChildList(list, t).size() > 0 ? true : false;
	}
}
