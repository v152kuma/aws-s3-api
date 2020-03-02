package com.kaamaaya.awsoperations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaamaaya.awsoperations.entity.Awsdata;

public interface AwsRepository extends JpaRepository<Awsdata, Integer> {

}
