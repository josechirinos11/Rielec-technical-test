package com.rielec.machine.test.backend.application.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;

    public class PageUtil {
        private static String SEPARATOR = ",";
        private static String SORTING_SEPARATOR = ":";

        public enum Order {
            ASC, DESC
        }

        /**
         * Creates a PageRequest with number page, size, and sort
         * @param page - Number of page
         * @param size - Size of each page
         * @param sortBy - fields to sort ASC|DESC
         * @return - Pageable to make request to repository
         */
        public static Pageable makeRequest(Integer page, Integer size, HashMap<String, Order> sortBy) {
            Sort sort = parseSort(sortBy);

            Pageable pageable = PageRequest.of(page, size, sort);
            return pageable;
        }

        public static HashMap<String, PageUtil.Order> getSorting(String sortBy) {
            HashMap<String, PageUtil.Order> sort = new HashMap<>();
            if (sortBy == null || sortBy.isEmpty()) return sort;

            String[] fields = sortBy.split(SEPARATOR);
            for(String field : fields) {
                String[] sortField = field.trim().split(SORTING_SEPARATOR);
                if (sortField.length == 2) {
                    PageUtil.Order order = PageUtil.Order.DESC.name().equals(sortField[1]) ? PageUtil.Order.DESC : PageUtil.Order.ASC;
                    sort.put(sortField[0], order);
                }
            }
            return sort;
        }
        public static Sort parseSort(String sortBy) {
            System.out.println(sortBy);
            HashMap<String, PageUtil.Order> sortArgs = getSorting(sortBy);
            return parseSort(sortArgs);
        }

        public static Sort parseSort(HashMap<String, PageUtil.Order> sortArgs) {
            final Sort[] sort = {null};

            if (sortArgs == null || sortArgs.isEmpty()) {
                sortArgs = new HashMap<>();
                sortArgs.put("createdDate", Order.DESC);
            }

            sortArgs.forEach((field, order) -> {
                if (sort[0] == null) {
                    if (order == Order.DESC) {
                        sort[0] = Sort.by(field).descending();
                    } else {
                        sort[0] = Sort.by(field).ascending();
                    }
                } else {
                    if (order == Order.DESC) {
                        sort[0] = sort[0].and(Sort.by(field).descending());
                    } else {
                        sort[0] = sort[0].and(Sort.by(field).ascending());
                    }
                }
            });

            return sort[0];
        }
    }