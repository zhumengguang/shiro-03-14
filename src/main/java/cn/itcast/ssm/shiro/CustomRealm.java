package cn.itcast.ssm.shiro;

import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.po.SysUser;
import cn.itcast.ssm.service.SysService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhumenguang
 * @Description:
 * @Date: Created in 11:13 2018/3/15
 * @Modified By:
 */
public class CustomRealm extends AuthorizingRealm {


    @Autowired
    private SysService sysService;


    @Override
    public void setName(String name) {
        super.setName("CustomRealm");
    }


    //用于授权

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

          //principalCollection 中获取认证信息

        ActiveUser activeUser = (ActiveUser) principalCollection.getPrimaryPrincipal();

        //根据身份信息获取权限信息
        //模拟从数据库获取到数据
        //List<String> permission = new ArrayList<String>();
        //
        //permission.add("user:create");//用户创建
        //permission.add("item:create");//商品创建
        //permission.add("item:query");//商品添加
        //permission.add("item:edit");//商品编辑

        //从数据库获取权限数据
        List<SysPermission> permissionList = null;
        try {
            permissionList = sysService.findPermissionListByUserId(activeUser.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //单独定义的集合
        List<String> permission = new ArrayList<String>();
        if(permissionList!=null){
            for(SysPermission sysPermission:permissionList){
                //将数据库中的权限标签存入集合中
                permission.add(sysPermission.getPercode());
            }
        }

        //查到权限数据,返回(要包括permissions)

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将你上边查询到的授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(permission);

        return simpleAuthorizationInfo;
    }




    //用于认证

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //token是从用户输入的
        //第一步从token中取出身份信息
       String userCode = (String) token.getPrincipal();

        SysUser sysUser = null;
        try {
             sysUser = sysService.findSysUserByUserCode(userCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(sysUser==null){

            return  null;
        }

        //第二步：根据用户输入的userCode从数据库中查询

        String password = sysUser.getPassword();
        //盐
        String salt = sysUser.getSalt();


        List<SysPermission> menList = null;
        try {
            menList = sysService.findMenuListByUserId(sysUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果查询不到返回null

        ActiveUser activeUser  =  new ActiveUser();
        activeUser.setUserid(sysUser.getId());
        activeUser.setMenus(menList);
        activeUser.setUsername(sysUser.getUsername());
        activeUser.setUsercode(sysUser.getUsercode());
       //如果查询到返回AuthenticationInfo
       //将activeUser设置到AuthenticationInfo
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(activeUser,password, ByteSource.Util.bytes(salt),this.getName());

        return simpleAuthenticationInfo;
    }

    //清除缓存
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
