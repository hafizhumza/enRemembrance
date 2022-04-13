package com.en.remembrance.dtos;

import com.en.remembrance.constants.PagePosition;
import com.en.remembrance.domain.Page;
import com.en.remembrance.domain.Segment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class PageModel {

    private Long id;

    private SegmentModel topLeftSegment;

    private SegmentModel topRightSegment;

    private SegmentModel bottomLeftSegment;

    private SegmentModel bottomRightSegment;

    public PageModel() {
        topLeftSegment = new SegmentModel();
        topRightSegment = new SegmentModel();
        bottomLeftSegment = new SegmentModel();
        bottomRightSegment = new SegmentModel();
    }

    public PageModel(Page page) {
        this.id = page.getId();

        List<Segment> segments = page.getSegments();
        if (!CollectionUtils.isEmpty(segments)) {
            for (Segment s : segments) {
                if (s.getPosition() == PagePosition.TOP_LEFT) {
                    topLeftSegment = new SegmentModel(s);
                } else if (s.getPosition() == PagePosition.TOP_RIGHT) {
                    topRightSegment = new SegmentModel(s);
                } else if (s.getPosition() == PagePosition.BOTTOM_LEFT) {
                    bottomLeftSegment = new SegmentModel(s);
                } else if (s.getPosition() == PagePosition.BOTTOM_RIGHT) {
                    bottomRightSegment = new SegmentModel(s);
                }
            }
        }
    }
}
