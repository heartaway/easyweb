package com.taobao.easyweb.core;

import java.util.LinkedList;
import java.util.List;

public class PaginationResult<T> {

	private boolean success;

	private int totalSize;

	private List<T> module;

	private int pageSize;

	private int currentPage;

	private int maxSlide;

	public PaginationResult() {
	}

	public PaginationResult(int pageNumber, int pageSize) {
		this.currentPage = pageNumber;
		this.pageSize = pageSize;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public List<T> getModule() {
		return module;
	}

	public void setModule(List<T> module) {
		this.module = module;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return (totalSize / pageSize) + 1;
	}

	public int getStart() {
		if (currentPage <= 1) {
			return 0;
		}
		return (currentPage - 1) * pageSize;
	}

	public boolean hasNext() {
		return getTotalPage() > currentPage;
	}

	public boolean hasPrev() {
		return currentPage != 1 && getTotalPage() > 1;
	}

	public int next() {
		return currentPage + 1;
	}

	public int prev() {
		return currentPage > 1 ? currentPage - 1 : 1;
	}

	public int getMaxSlide() {
		return maxSlide;
	}

	public void setMaxSlide(int maxSlide) {
		this.maxSlide = maxSlide;
	}

	public List<Object> getSlider(){
		int curr = currentPage;
		int total = getTotalPage();
		curr = (curr < 1)?1:curr;
		curr = (curr > total)?total:curr;
		total = (total < 1)?1:total;
		
		List<Object> l = new LinkedList<Object>();
		int lstart = 1;
		int mtotal = 9;
		int prefix = 2;
		int lend = total>1?prefix:1; // 如果总共只有1页
		
		int mstart = curr - 2;
		int mend = curr +4;
		
		if(total <= 9){
			mend = total;
			mstart = lend + 1;
		}
		
		if(mend > total ){
			mend = total;
			mstart = total - mtotal + prefix + 1;//最后页数超过总页数
		}
		
		if(mstart <= lend){
			mstart = lend + 1; // 从第三页开始 
		}
		
		if(mend <= mtotal && total >=mtotal){
			mend = mtotal; // 总页数不足9页的情况，补足
		}
		
		l.add(lstart);
		if(lend>1){
			l.add(lend);
		}
		if(lend < mstart - 1){
			l.add("...");
		}
		
		for(int i = mstart;i<=mend;i++){
			l.add(i);
		}
		
		if(mend < total){
			l.add("...");
		}
		return l;
	}

}
