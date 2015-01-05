package com.enlight.game.base.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

public class DefaultRepositoryFactory extends JpaRepositoryFactory {

	private final EntityManager entityManager;

	public DefaultRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
		this.entityManager = entityManager;
	}

	@Override
	protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository(
			RepositoryMetadata metadata, EntityManager entityManager) {

		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata
				.getDomainType());
		return new GenericRepositoryImpl(entityInformation, entityManager); // custom
																			// implementation
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

		return GenericRepositoryImpl.class;
	}
}
