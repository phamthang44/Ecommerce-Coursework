package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.UserDetailsResponse;
import com.greenwich.ecommerce.entity.Address;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.repository.criteria.ProductSearchCriteriaQueryConsumer;
import com.greenwich.ecommerce.repository.criteria.SearchCriteria;
import com.greenwich.ecommerce.repository.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchRepository {

    @PersistenceContext
    private EntityManager em;

    public PageResponse<?> searchProducts(int pageNo, int pageSize, String... search) {
        int page = pageNo;
        if (pageNo > 0) {
            page = pageNo - 1;
        }
        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); //rõ ràng regex ra
            for (String s : search) {
                //name:value
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List products = getProducts(pageNo, pageSize, criteriaList);

        Long totalElements = getTotalProductElements(criteriaList);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(totalElements.intValue())
                .items(products)
                .build();
    }

    private Long getTotalProductElements(List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<?> productRoot = criteriaQuery.from(Product.class);

        Predicate predicate = builder.conjunction();


        criteriaQuery.select(builder.count(productRoot));
        criteriaQuery.where(predicate);

        return em.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Product> getProducts(int pageNo, int pageSize, List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        Predicate predicate = builder.conjunction();
        ProductSearchCriteriaQueryConsumer queryConsumer = new ProductSearchCriteriaQueryConsumer(builder, predicate, productRoot);

        return em.createQuery(criteriaQuery).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }
}
