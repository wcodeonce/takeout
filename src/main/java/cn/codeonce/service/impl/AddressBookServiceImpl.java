package cn.codeonce.service.impl;

import cn.codeonce.mapper.AddressBookMapper;
import cn.codeonce.pojo.AddressBook;
import cn.codeonce.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * codeonce
 * AddressBookServiceImpl
 * TakeOut
 * 2022/5/13
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
