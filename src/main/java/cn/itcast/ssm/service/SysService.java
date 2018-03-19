package cn.itcast.ssm.service;

import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.po.SysUser;

import java.util.List;

/**
 * 
 * <p>Title: SysService</p>
 * <p>Description: 认证授权服务接口</p>
 * <p>Company: www.itcast.com</p> 
 * @author	传智.燕青
 * @date	2015-3-23上午11:29:48
 * @version 1.0
 */
public interface SysService {
	
	//根据用户的身份和密码 进行认证，如果认证通过，返回用户身份信息
	public ActiveUser authenticat(String userCode, String password) throws Exception;
	
	//根据用户账号查询用户信息
	public SysUser findSysUserByUserCode(String userCode)throws Exception;
	
	//根据用户id查询权限范围的菜单
	public List<SysPermission> findMenuListByUserId(String userid) throws Exception;
	
	//根据用户id查询权限范围的url
	public List<SysPermission> findPermissionListByUserId(String userid) throws Exception;





	//根据用户的身份和密码进行认证，如果认证通过，返回用户的身份信息

    public ActiveUser renzheng(String userCode,String password) throws Exception;



	public SysUser findUserByUserCode(String userCode)throws Exception;

	//根据用户的id查询权限范围的菜单

	public List<SysPermission> findMenuListByUid(String userId)throws Exception;

	//根据用户的id查询权限范围的url

	public List<SysPermission> findPermissionListByUid(String userId)throws  Exception;













}
