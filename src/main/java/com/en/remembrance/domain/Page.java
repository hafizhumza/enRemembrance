package com.en.remembrance.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "page")
public class Page implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Segment> segments;

    @ManyToOne
    @JoinColumn(name = "story_book_id", referencedColumnName = "id")
    private StoryBook storyBook;

    public void addSegments(List<Segment> segmentList) {
        if (segments == null) {
            segments = new ArrayList<>();
        }

        segments.addAll(segmentList);
    }

    public void addSegment(Segment segment) {
        if (segments == null) {
            segments = new ArrayList<>();
        }

        segments.add(segment);
    }

}
