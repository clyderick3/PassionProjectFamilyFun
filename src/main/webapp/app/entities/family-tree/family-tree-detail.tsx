import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './family-tree.reducer';

export const FamilyTreeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const familyTreeEntity = useAppSelector(state => state.familyTree.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="familyTreeDetailsHeading">
          <Translate contentKey="familyfunApp.familyTree.detail.title">FamilyTree</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{familyTreeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="familyfunApp.familyTree.name">Name</Translate>
            </span>
          </dt>
          <dd>{familyTreeEntity.name}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="familyfunApp.familyTree.age">Age</Translate>
            </span>
          </dt>
          <dd>{familyTreeEntity.age}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="familyfunApp.familyTree.location">Location</Translate>
            </span>
          </dt>
          <dd>{familyTreeEntity.location}</dd>
          <dt>
            <Translate contentKey="familyfunApp.familyTree.user">User</Translate>
          </dt>
          <dd>
            {familyTreeEntity.users
              ? familyTreeEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {familyTreeEntity.users && i === familyTreeEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="familyfunApp.familyTree.photo">Photo</Translate>
          </dt>
          <dd>{familyTreeEntity.photo ? familyTreeEntity.photo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/family-tree" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/family-tree/${familyTreeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FamilyTreeDetail;
