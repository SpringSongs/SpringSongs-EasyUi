package io.github.springsongs.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.springsongs.domain.SpringDictionaryDetail;
import io.github.springsongs.domain.dto.SpringDictionaryDetailDTO;
import io.github.springsongs.domain.query.SpringDictionaryDetailQuery;
import io.github.springsongs.service.ISpringDictionaryDetailService;
import io.github.springsongs.util.Constant;
import io.github.springsongs.util.IpKit;
import io.github.springsongs.util.R;

@RestController
@RequestMapping(value = "/SpringDictionaryDetail")
public class SpringDictionaryDetailController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SpringDictionaryDetailController.class);

	@Autowired
	private ISpringDictionaryDetailService springDictionaryDetailService;

	@PostMapping(value = "ListByPage")
	public R listByPage(@RequestBody SpringDictionaryDetailQuery springDictionaryDetailQuery,
			@PageableDefault(page = 1, size = 20) Pageable pageable) {
		R r = new R();
		try {
			Page<SpringDictionaryDetailDTO> lists = springDictionaryDetailService
					.getAllRecordByPage(springDictionaryDetailQuery, pageable);
			r.put("code", HttpServletResponse.SC_OK);
			r.put("msg", Constant.SELECT_SUCCESSED);
			r.put("data", lists.getContent());
			r.put("count", lists.getTotalElements());
		} catch (Exception e) {
			r.put("msg", Constant.SYSTEM_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
			logger.error(e.getMessage());
		}
		return r;
	}

	@PostMapping(value = "/Detail")
	public R get(@NotEmpty(message = "id不能为空") String id) {
		R r = new R();
		if (StringUtils.isEmpty(id)) {
			r.put("msg", Constant.PARAMETER_NOT_NULL_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				SpringDictionaryDetail entity = springDictionaryDetailService.selectByPrimaryKey(id);
				r.put("msg", Constant.SELECT_SUCCESSED);
				r.put("code", HttpServletResponse.SC_OK);
				r.put("data", entity);
			} catch (Exception e) {
				r.put("msg", Constant.SYSTEM_ERROR);
				r.put("code", HttpServletResponse.SC_BAD_REQUEST);
				logger.error(e.getMessage());
			}
		}
		return r;
	}

	@PostMapping(value = "/Create")
	public R save(@RequestBody @Valid SpringDictionaryDetailDTO viewEntity, HttpServletRequest request) {
		R r = new R();
		try {
			viewEntity.setCreatedBy(this.getUser().getUserName());
			viewEntity.setCreatedUserId(this.getUser().getId());
			viewEntity.setCreatedIp(IpKit.getRealIp(request));
			viewEntity.setCreatedOn(new Date());
			springDictionaryDetailService.insert(viewEntity);
			r.put("msg", Constant.SAVE_SUCCESSED);
			r.put("code", HttpServletResponse.SC_OK);
		} catch (Exception e) {
			r.put("msg", Constant.SYSTEM_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
			logger.error(e.getMessage());
		}
		return r;
	}

	@PostMapping(value = "/Edit")
	public R update(@RequestBody @Valid SpringDictionaryDetailDTO viewEntity, HttpServletRequest request) {
		R r = new R();

		try {
			SpringDictionaryDetailDTO entity = springDictionaryDetailService.selectByPrimaryKey(viewEntity.getId());
			if (null == entity) {
				r.put("msg", Constant.INFO_NOT_FOUND);
				r.put("code", HttpServletResponse.SC_BAD_REQUEST);
			} else {
				entity.setId(viewEntity.getId());
				entity.setDictionaryCode(viewEntity.getDictionaryCode());
				entity.setParentId(viewEntity.getParentId());
				entity.setDetailCode(viewEntity.getDetailCode());
				entity.setDetailName(viewEntity.getDetailName());
				entity.setDetailValue(viewEntity.getDetailValue());
				entity.setDescription(viewEntity.getDescription());
				entity.setChildIds(viewEntity.getChildIds());
				entity.setSortCode(viewEntity.getSortCode());
				entity.setEnableEdit(viewEntity.getEnableEdit());
				entity.setEnableDelete(viewEntity.getEnableDelete());
				entity.setDeletedStatus(viewEntity.getDeletedStatus());
				entity.setUpdatedOn(new Date());
				entity.setUpdatedUserId(this.getUser().getId());
				entity.setUpdatedBy(this.getUser().getUserName());
				entity.setUpdatedIp(IpKit.getRealIp(request));
				springDictionaryDetailService.updateByPrimaryKey(entity);
				r.put("msg", Constant.SAVE_SUCCESSED);
				r.put("code", HttpServletResponse.SC_OK);
			}
		} catch (Exception e) {
			r.put("msg", Constant.SYSTEM_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
			logger.error(e.getMessage());
		}
		return r;
	}

	@PostMapping(value = "/SetDeleted")
	public R setDeleted(@RequestParam(value = "ids", required = true) List<String> ids) {
		R r = new R();
		if (CollectionUtils.isEmpty(ids)) {
			r.put("msg", Constant.PARAMETER_NOT_NULL_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				springDictionaryDetailService.setDeleted(ids);
				r.put("msg", Constant.DELETE_SUCCESSED);
				r.put("code", HttpServletResponse.SC_OK);
			} catch (Exception e) {
				r.put("msg", Constant.SYSTEM_ERROR);
				r.put("code", HttpServletResponse.SC_BAD_REQUEST);
				logger.error(e.getMessage());
			}
		}
		return r;
	}

	@PostMapping(value = "/Deleted")
	public R deleted(@RequestParam(value = "ids", required = true) List<String> ids) {
		R r = new R();
		if (CollectionUtils.isEmpty(ids)) {
			r.put("msg", Constant.PARAMETER_NOT_NULL_ERROR);
			r.put("code", HttpServletResponse.SC_BAD_REQUEST);
		} else {
			try {
				springDictionaryDetailService.delete(ids);
				r.put("msg", Constant.DELETE_SUCCESSED);
				r.put("code", HttpServletResponse.SC_OK);
			} catch (Exception e) {
				r.put("msg", Constant.SYSTEM_ERROR);
				r.put("code", HttpServletResponse.SC_BAD_REQUEST);
				logger.error(e.getMessage());
			}
		}
		return r;
	}
}