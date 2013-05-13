package com.taobao.easyweb.remark;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.easyweb.core.PaginationResult;
import com.taobao.easyweb.core.Result;
import com.taobao.easyweb.core.context.ThreadContext;
import com.taobao.easyweb.security.domain.User;

@Component("ewRemarkManager")
public class RemarkManager {

	@Resource(name = "ewRemarkDAO")
	private RemarkDAO remarkDAO;

	public Result<String> addRemark(Remark remark) {
		Result<String> result = new Result<String>(true);
		User user = (User) ThreadContext.getContext().getContext("user");
		if (user != null) {
			remark.setCreator(user.getName());
			remark.setCreatorId(user.getId());
		}
		remarkDAO.insert(remark);
		return result;
	}

	public Result<String> updateRemark(Remark remark) {
		Result<String> result = new Result<String>(true);
		remarkDAO.update(remark);
		return result;
	}

	public Result<String> delete(Integer id) {
		Result<String> result = new Result<String>(true);
		Remark remark = remarkDAO.queryById(id);
		if (remark != null) {
			remark.setStatus(-1);
			remarkDAO.update(remark);
		}
		return result;
	}

	public PaginationResult<Remark> queryRootRemark(Integer type, int pageNumber) {
		PaginationResult<Remark> result = new PaginationResult<Remark>(pageNumber, 10);
		result.setTotalSize(remarkDAO.queryRootCount(type));
		result.setModule(remarkDAO.queryRootRemark(type, result.getStart(), result.getPageSize()));
		return result;
	}

	public Result<List<Remark>> queryTitleLike(String titleLike) {
		Result<List<Remark>> result = new Result<List<Remark>>();
		result.setModule(remarkDAO.queryTitleLike(titleLike));
		return result;
	}

	public Result<Remark> queryDetail(Integer id) {
		Result<Remark> result = new Result<Remark>(false);
		Remark remark = remarkDAO.queryById(id);
		if (remark == null) {
			result.addErrorMessage("ÄÚÈÝ²»´æÔÚ");
			return result;
		}
		List<Remark> children = remarkDAO.queryChildrenRemarks(id);
		for (Remark r : children) {
			List<Remark> subChildren = remarkDAO.queryChildrenRemarks(r.getId());
			r.setChildren(subChildren);
		}
		remark.setChildren(children);
		result.setModule(remark);
		result.setSuccess(true);
		return result;
	}

	public List<Remark> queryRemarks(Remark remark) {
		return remarkDAO.queryRemarks(remark);
	}

	public List<Remark> queryChildrenRemarks(Integer id) {
		return remarkDAO.queryChildrenRemarks(id);
	}

}
