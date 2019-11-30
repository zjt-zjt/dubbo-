package com.taotao.common;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EUTreeNode {
    /*节点id*/
    private Long id;
    /*节点名称*/
    private String text;
    /*节点状态*/
    private String state;

}
