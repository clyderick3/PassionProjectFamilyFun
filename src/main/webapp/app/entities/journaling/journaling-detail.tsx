import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './journaling.reducer';

export const JournalingDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const journalingEntity = useAppSelector(state => state.journaling.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="journalingDetailsHeading">
          <Translate contentKey="familyfunApp.journaling.detail.title">Journaling</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{journalingEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="familyfunApp.journaling.title">Title</Translate>
            </span>
          </dt>
          <dd>{journalingEntity.title}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="familyfunApp.journaling.date">Date</Translate>
            </span>
          </dt>
          <dd>{journalingEntity.date}</dd>
          <dt>
            <span id="journalEntry">
              <Translate contentKey="familyfunApp.journaling.journalEntry">Journal Entry</Translate>
            </span>
          </dt>
          <dd>{journalingEntity.journalEntry}</dd>
          <dt>
            <Translate contentKey="familyfunApp.journaling.familyTree">Family Tree</Translate>
          </dt>
          <dd>
            {journalingEntity.familyTrees
              ? journalingEntity.familyTrees.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {journalingEntity.familyTrees && i === journalingEntity.familyTrees.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/journaling" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/journaling/${journalingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JournalingDetail;
