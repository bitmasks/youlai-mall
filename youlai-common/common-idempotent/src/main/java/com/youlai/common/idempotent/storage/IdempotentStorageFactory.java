package com.youlai.common.idempotent.storage;

import com.youlai.common.idempotent.enums.IdempotentStorageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class IdempotentStorageFactory {

    @Autowired
    private List<IdempotentStorage> idempotentStorageList;

    public IdempotentStorage getIdempotentStorage(IdempotentStorageTypeEnum type) {
        Optional<IdempotentStorage> idempotentStorageOptional = idempotentStorageList.stream().filter(t -> t.type() == type).findAny();
        return idempotentStorageOptional.orElseThrow(NullPointerException::new);
    }

}
