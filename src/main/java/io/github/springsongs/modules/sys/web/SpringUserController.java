package io.github.springsongs.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.springsongs.common.dto.ReponseResultPageDTO;
import io.github.springsongs.common.dto.ResponseDTO;
import io.github.springsongs.common.web.BaseController;
import io.github.springsongs.enumeration.ResultCode;
import io.github.springsongs.modules.sys.domain.SpringUser;
import io.github.springsongs.modules.sys.domain.SpringUserRole;
import io.github.springsongs.modules.sys.domain.SpringUserSecurity;
import io.github.springsongs.modules.sys.dto.MenuDTO;
import io.github.springsongs.modules.sys.dto.SpringUserDTO;
import io.github.springsongs.modules.sys.dto.UserInfoDTO;
import io.github.springsongs.modules.sys.dto.query.SpringUserQuery;
import io.github.springsongs.modules.sys.service.ISpringResourceService;
import io.github.springsongs.modules.sys.service.ISpringUserService;
import io.github.springsongs.util.HttpUtils;
import io.github.springsongs.util.IpKit;

@RestController
@RequestMapping(value = "/SpringUser")
public class SpringUserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SpringUserController.class);

	@Autowired
	private ISpringUserService springUserService;
	
	@Autowired
	private ISpringResourceService springResourceService;

	@PostMapping(value = "Invalidate")
	public ResponseDTO<String> invalidateSession(HttpServletRequest reqeust, HttpServletResponse response) {
		if (HttpUtils.isAjaxRequest(reqeust)) {
			return ResponseDTO.successed(null, ResultCode.SESSION_HAS_GONE);
		} else {
			try {
				response.sendRedirect("/login");
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return ResponseDTO.successed(null, ResultCode.SESSION_HAS_GONE);
	}
	
	@GetMapping(value = "/GetUserInfo")
	public ResponseDTO<UserInfoDTO> getUserInfo(){
		UserInfoDTO userInfoDTO=new UserInfoDTO();
		userInfoDTO.setRoles(this.getAuth());
		List<MenuDTO> menuList = springResourceService.ListModuleByUserId(this.getUser().getId());
		userInfoDTO.setMenuDTOs(menuList);
		return ResponseDTO.successed(userInfoDTO, ResultCode.SELECT_SUCCESSED);
	}

	@PostMapping(value = "/ListByPage")
	public ReponseResultPageDTO<SpringUserDTO> listByPage(@RequestBody SpringUserQuery springUserQuery,
			@PageableDefault(page = 1, size = 20) Pageable pageable) {
		Page<SpringUserDTO> lists = springUserService.getAllRecordByPage(springUserQuery, pageable);
		return ReponseResultPageDTO.successed(lists.getContent(), lists.getTotalElements(),
				ResultCode.SELECT_SUCCESSED);
	}

	@PostMapping(value = "/ListByRoleId/{roleId}")
	public ReponseResultPageDTO<SpringUserDTO> listByRoleId(
			@PathVariable(value = "roleId", required = true) String roleId,
			@PageableDefault(page = 1, size = 20) Pageable pageable) {
		Page<SpringUserDTO> lists = springUserService.ListUsersByRoleId(roleId, pageable);
		return ReponseResultPageDTO.successed(lists.getContent(), lists.getTotalElements(),
				ResultCode.SELECT_SUCCESSED);
	}

	@PostMapping(value = "/Detail")
	public ResponseDTO<String> get(@NotEmpty(message = "id不能为空") String id) {
		SpringUser entity = springUserService.selectByPrimaryKey(id);
		return ResponseDTO.successed(entity, ResultCode.SELECT_SUCCESSED);
	}

	@PostMapping(value = "/Create")
	public ResponseDTO<String> save(@RequestBody @Valid SpringUserDTO viewEntity, HttpServletRequest request) {
		SpringUserDTO entity = springUserService.getByUserName(viewEntity.getUserName().trim());
		if (null != entity) {
			return ResponseDTO.successed(entity, ResultCode.ACCOUNT_HAS_REGISTER);
		} else {
			viewEntity.setCreatedBy(this.getUser().getUserName());
			viewEntity.setCreatedUserId(this.getUser().getId());
			viewEntity.setCreatedIp(IpKit.getRealIp(request));
			viewEntity.setCreatedOn(new Date());
			springUserService.insert(viewEntity);
			return ResponseDTO.successed(entity, ResultCode.SAVE_SUCCESSED);
		}
	}

	@PostMapping(value = "/Edit")
	public ResponseDTO<String> update(@RequestBody @Valid SpringUserDTO viewEntity, HttpServletRequest request) {
		viewEntity.setUpdatedOn(new Date());
		viewEntity.setUpdatedUserId(this.getUser().getId());
		viewEntity.setUpdatedBy(this.getUser().getUserName());
		viewEntity.setUpdatedIp(IpKit.getRealIp(request));
		springUserService.updateByPrimaryKey(viewEntity);
		return ResponseDTO.successed(null, ResultCode.UPDATE_SUCCESSED);
	}

	@PostMapping(value = "/SetDeleted")
	public ResponseDTO<String> setDeleted(@RequestParam(value = "ids", required = true) List<String> ids) {
		springUserService.setDeleted(ids);
		return ResponseDTO.successed(null, ResultCode.DELETE_SUCCESSED);
	}

	@PostMapping(value = "/Deleted")
	public ResponseDTO<String> deleted(@RequestParam(value = "ids", required = true) List<String> ids) {

		springUserService.setDeleted(ids);
		return ResponseDTO.successed(null, ResultCode.DELETE_SUCCESSED);
	}

	@PostMapping(value = "/SetPwd")
	public ResponseDTO<String> SetPwd(@RequestBody SpringUserSecurity viewEntity, HttpServletRequest request) {
		
		if (StringUtils.isEmpty(viewEntity.getUserId())) {
			return ResponseDTO.successed(null, ResultCode.PARAMETER_NOT_NULL_ERROR);
		} else if (StringUtils.isEmpty(viewEntity.getPwd())) {
			return ResponseDTO.successed(null, ResultCode.PASSWORD_CAN_NOT_EMPTY);
		} else {
			viewEntity.setCreatedBy(this.getUser().getUserName());
			viewEntity.setCreatedUserId(this.getUser().getId());
			viewEntity.setCreatedIp(IpKit.getRealIp(request));
			viewEntity.setCreatedOn(new Date());
			viewEntity.setUpdatedOn(new Date());
			viewEntity.setUpdatedUserId(this.getUser().getId());
			viewEntity.setUpdatedBy(this.getUser().getUserName());
			viewEntity.setUpdatedIp(IpKit.getRealIp(request));
			springUserService.setPwd(viewEntity);
			return ResponseDTO.successed(null, ResultCode.SAVE_SUCCESSED);
		}
	}

	@PostMapping(value = "/SetRoles/{userId}")
	public ResponseDTO<String> setUsers(@PathVariable(value = "userId", required = true) String userId,
			@RequestParam(value = "ids", required = true) List<String> roleIds, HttpServletRequest request) {
		List<SpringUserRole> baseUserRoleEntityList = new ArrayList<SpringUserRole>();
		for (String str : roleIds) {
			SpringUserRole entity = new SpringUserRole();
			entity.setRoleId(str);
			entity.setUserId(userId);
			entity.setCreatedBy(this.getUser().getUserName());
			entity.setCreatedUserId(this.getUser().getId());
			entity.setCreatedIp(IpKit.getRealIp(request));
			entity.setCreatedOn(new Date());
			baseUserRoleEntityList.add(entity);
		}
		springUserService.saveUserToRole(baseUserRoleEntityList, userId);
		return ResponseDTO.successed(null, ResultCode.SAVE_SUCCESSED);
	}

}
