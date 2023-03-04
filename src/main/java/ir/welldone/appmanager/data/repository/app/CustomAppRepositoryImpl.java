package ir.welldone.appmanager.data.repository.app;

import ir.welldone.appmanager.data.entity.App;
import ir.welldone.appmanager.view.dto.AppDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomAppRepositoryImpl implements CustomAppRepository {

    @PersistenceContext
    private EntityManager em;

    private static String SEARCH_QUERY = "SELECT a FROM App a WHERE 1 = 1 ";

    @Override
    public List<App> search(AppDTO criteria, int first, int size) {
        log.debug("Searching for Apps, from {} to {}", first, size);
        StringBuilder jpqlQuery = new StringBuilder(SEARCH_QUERY);
        Map<String, Object> properties = new HashMap<>();
        if(criteria.getName() != null){
            jpqlQuery.append("AND a.name LIKE :name ");
            properties.put("name", "%" + criteria.getName() + "%");
        }
        if(criteria.getDesc() != null){
            jpqlQuery.append("AND a.desc LIKE :desc ");
            properties.put("desc", "%" + criteria.getDesc() + "%");
        }
        if(criteria.getDomain() != null){
            jpqlQuery.append("AND a.domain LIKE :domain ");
            properties.put("domain", "%" + criteria.getDomain() + "%");
        }
        if(criteria.getEnabled() != null){
            jpqlQuery.append("AND a.enabled = :enabled ");
            properties.put("enabled", criteria.getEnabled());
        }
        jpqlQuery.append(" ORDER BY a.name ");
        TypedQuery<App> typedQuery = em.createQuery(jpqlQuery.toString(), App.class);
        setQueryProperties(typedQuery, properties);
        typedQuery.setFirstResult(first);
        typedQuery.setMaxResults(size);
        List<App> result = typedQuery.getResultList();
        log.debug("Fetched {} records", result.size());
        return result;
    }

    private static String COUNT_QUERY = "SELECT count(a) FROM App a WHERE 1 = 1 ";

    @Override
    public Long count(AppDTO criteria) {
        log.debug("Calculating total records");
        StringBuilder jpqlQuery = new StringBuilder(COUNT_QUERY);
        Map<String, Object> properties = new HashMap<>();
        if(criteria.getName() != null){
            jpqlQuery.append("AND a.name LIKE :name ");
            properties.put("name", "%" + criteria.getName() + "%");
        }
        if(criteria.getDesc() != null){
            jpqlQuery.append("AND a.desc LIKE :desc ");
            properties.put("desc", "%" + criteria.getDesc() + "%");
        }
        if(criteria.getDomain() != null){
            jpqlQuery.append("AND a.domain LIKE :domain ");
            properties.put("domain", "%" + criteria.getDomain() + "%");
        }
        if(criteria.getEnabled() != null){
            jpqlQuery.append("AND a.enabled = :enabled ");
            properties.put("enabled", criteria.getEnabled());
        }

        Query query = em.createQuery(jpqlQuery.toString());
        setQueryProperties(query, properties);
        Long count = (Long) query.getSingleResult();
        log.debug("{} records found", count);
        return count;
    }

    private void setQueryProperties(Query query, Map<String, Object> properties){
        properties.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
    }
}
