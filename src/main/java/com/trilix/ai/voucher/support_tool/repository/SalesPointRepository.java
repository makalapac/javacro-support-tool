package com.trilix.ai.voucher.support_tool.repository;

import com.trilix.ai.voucher.support_tool.entity.Salespoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesPointRepository extends JpaRepository<Salespoint, Integer> {

     Optional<Salespoint> findByName(String salesPointName);
}
