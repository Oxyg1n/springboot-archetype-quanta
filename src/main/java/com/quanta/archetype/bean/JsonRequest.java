package com.quanta.archetype.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 过期@Deprecated：前端需要在传入的数据外套入一层data，比较少用
 * 数据接收主类
 * <T>填入要自动注入的DTO
 *
 * @author quanta
 */
@Deprecated
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonRequest<T> {
    @NotNull
    @Valid
    T data;
}
