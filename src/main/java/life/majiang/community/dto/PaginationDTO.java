package life.majiang.community.dto;

import life.majiang.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;// 装集合
    private boolean showPrevious;//上一页
    private boolean showFirstpage;//第一页
    private boolean showNext;//下一页
    private boolean showEndpage;//最后一页
    private Integer page;//当前页数
    private List<Integer> pages = new ArrayList<>();//装需要几个数组
    private Integer totalpage;//所有页数

    public void setpaginationDTO(Integer totalpage, Integer page, Integer size) {
        this.totalpage = totalpage;

        this.page = page;
        pages.add(page);
        for (int i = 1; i < 4; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalpage) {
                pages.add(page + i);
            }
        }
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        if (page == totalpage) {
            showNext = false;
        } else {
            showNext = true;
        }
        if (pages.contains(1)) {
            showFirstpage = false;
        } else {
            showFirstpage = true;
        }
        if (pages.contains(totalpage)) {
            showEndpage = false;
        } else {
            showEndpage = true;
        }


    }
}
