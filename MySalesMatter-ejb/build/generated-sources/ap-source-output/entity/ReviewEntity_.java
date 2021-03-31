package entity;

import entity.ListingEntity;
import entity.UserEntity;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-28T23:01:26")
@StaticMetamodel(ReviewEntity.class)
public class ReviewEntity_ { 

    public static volatile SingularAttribute<ReviewEntity, String> descripion;
    public static volatile SingularAttribute<ReviewEntity, UserEntity> reviewer;
    public static volatile SingularAttribute<ReviewEntity, Integer> starRating;
    public static volatile SingularAttribute<ReviewEntity, ListingEntity> listing;
    public static volatile SingularAttribute<ReviewEntity, Long> reviewId;
    public static volatile SingularAttribute<ReviewEntity, List> picturePaths;

}