package cn.codeonce.controller;

import cn.codeonce.common.BaseContext;
import cn.codeonce.common.R;
import cn.codeonce.pojo.AddressBook;
import cn.codeonce.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@Api(tags = "地址簿相关接口")
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增用户信息
     *
     * @param addressBook 用户信息
     * @return 保存成功的用户信息
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("新增用户信息=>{}", addressBook);
        // 执行保存操作
        addressBookService.save(addressBook);
        // 操作响应
        return R.success(addressBook);
    }


    /**
     * 更新用户信息
     *
     * @param addressBook 用户新信息
     * @return 操作响应
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("更新用户信息=>{}", addressBook);
        // 执行保存操作
        addressBookService.updateById(addressBook);
        // 操作响应
        return R.success("用户信息更新成功!");
    }


    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息
     * @return 操作响应
     */
    @PutMapping("default")
    @Transactional
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("要变更地址信息=>{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        // 修改所有地址的 Default 为 0
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        // 将用户要设置的地址的 Default 设置为 1
        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        // 执行更新操作
        addressBookService.updateById(addressBook);
        // 操作响应
        return R.success(addressBook);
    }


    /**
     * 根据ID查找对应的地址
     *
     * @param id 地址id
     * @return 操作响应
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        // 执行查询返回数据
        AddressBook addressBook = addressBookService.getById(id);

        // 判断是否找到地址 如果为空则返回未找到
        if (addressBook == null) return R.error("没有找到该对象");

        // 找到则返回改地址
        return R.success(addressBook);
    }


    /**
     * 查询默认地址
     *
     * @return 查询到的默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        // 条件构造
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        // 查找地址
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        // 未找到该地址
        if (null == addressBook) return R.error("没有找到该对象");

        // 找到改地址则返回该地址
        return R.success(addressBook);
    }


    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("查询用户的地址=>{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        // 执行查询用户全部地址
        //SQL:select * from address_book where user_id = ? order by update_time desc
        List<AddressBook> list = addressBookService.list(queryWrapper);

        // 返回找到的数据
        return R.success(list);
    }
}
