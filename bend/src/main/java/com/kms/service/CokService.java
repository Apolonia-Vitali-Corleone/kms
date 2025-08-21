package com.kms.service;

import com.kms.entity.CokDO;
import com.kms.entity.TagDO;

import java.util.List;

public interface CokService {
    List<CokDO> listByKId(Long id);
}
