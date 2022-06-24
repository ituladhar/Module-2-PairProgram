package com.techelevator.dao;

import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
                    "FROM site " +
                    "JOIN campground USING (campground_id) " +
                    "JOIN park USING (park_id) " +
                    "WHERE park_id = ? AND (accessible = true AND max_rv_length > 0);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            sites.add(mapRowToSite(results));
        }
        return sites;
    }


    public List<Site> getAvailableSites(int parkId) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
                    "FROM site " +
                    "LEFT JOIN reservation USING(site_id) " +
                    "JOIN campground USING(campground_id) " +
                    "WHERE campground.park_id = ? AND site.site_id NOT IN (SELECT site.site_id " +
                    "FROM site " +
                    "LEFT JOIN reservation ON site.site_id = reservation.site_id " +
                    "JOIN campground ON site.campground_id = campground.campground_id " +
                    "WHERE from_date < current_date AND  to_date > current_date) AND  from_date IS NULL;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            sites.add(mapRowToSite(results));
        } return sites;
    }

    public List<Site> getAvailableSitesPerDates(int parkId, String fromDate, String toDate) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
                    "FROM site " +
                    "LEFT JOIN reservation USING(site_id) " +
                    "JOIN campground USING(campground_id) " +
                    "WHERE park_id = ? AND (reservation_id IS NULL OR (current_date NOT BETWEEN ? AND ?)= NULL);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId, fromDate, toDate);
        while (results.next()) {
            sites.add(mapRowToSite(results));
        } return sites;
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }

}
