import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFamilyTree } from 'app/shared/model/family-tree.model';
import { getEntities } from './family-tree.reducer';

export const FamilyTree = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const familyTreeList = useAppSelector(state => state.familyTree.entities);
  const loading = useAppSelector(state => state.familyTree.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="family-tree-heading" data-cy="FamilyTreeHeading">
        <Translate contentKey="familyfunApp.familyTree.home.title">Family Trees</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="familyfunApp.familyTree.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/family-tree/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="familyfunApp.familyTree.home.createLabel">Create new Family Tree</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {familyTreeList && familyTreeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.age">Age</Translate>
                </th>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="familyfunApp.familyTree.photo">Photo</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {familyTreeList.map((familyTree, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/family-tree/${familyTree.id}`} color="link" size="sm">
                      {familyTree.id}
                    </Button>
                  </td>
                  <td>{familyTree.name}</td>
                  <td>{familyTree.age}</td>
                  <td>{familyTree.location}</td>
                  <td>
                    {familyTree.users
                      ? familyTree.users.map((val, j) => (
                          <span key={j}>
                            {val.id}
                            {j === familyTree.users.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{familyTree.photo ? <Link to={`/photo/${familyTree.photo.id}`}>{familyTree.photo.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/family-tree/${familyTree.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/family-tree/${familyTree.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/family-tree/${familyTree.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="familyfunApp.familyTree.home.notFound">No Family Trees found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FamilyTree;
