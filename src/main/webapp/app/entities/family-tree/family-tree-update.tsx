import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPhoto } from 'app/shared/model/photo.model';
import { getEntities as getPhotos } from 'app/entities/photo/photo.reducer';
import { IJournaling } from 'app/shared/model/journaling.model';
import { getEntities as getJournalings } from 'app/entities/journaling/journaling.reducer';
import { IAlbum } from 'app/shared/model/album.model';
import { getEntities as getAlbums } from 'app/entities/album/album.reducer';
import { IFamilyTree } from 'app/shared/model/family-tree.model';
import { getEntity, updateEntity, createEntity, reset } from './family-tree.reducer';

export const FamilyTreeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const photos = useAppSelector(state => state.photo.entities);
  const journalings = useAppSelector(state => state.journaling.entities);
  const albums = useAppSelector(state => state.album.entities);
  const familyTreeEntity = useAppSelector(state => state.familyTree.entity);
  const loading = useAppSelector(state => state.familyTree.loading);
  const updating = useAppSelector(state => state.familyTree.updating);
  const updateSuccess = useAppSelector(state => state.familyTree.updateSuccess);
  const handleClose = () => {
    props.history.push('/family-tree');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getPhotos({}));
    dispatch(getJournalings({}));
    dispatch(getAlbums({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...familyTreeEntity,
      ...values,
      users: mapIdList(values.users),
      photo: photos.find(it => it.id.toString() === values.photo.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...familyTreeEntity,
          users: familyTreeEntity?.users?.map(e => e.id.toString()),
          photo: familyTreeEntity?.photo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="familyfunApp.familyTree.home.createOrEditLabel" data-cy="FamilyTreeCreateUpdateHeading">
            <Translate contentKey="familyfunApp.familyTree.home.createOrEditLabel">Create or edit a FamilyTree</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="family-tree-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('familyfunApp.familyTree.name')}
                id="family-tree-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField label={translate('familyfunApp.familyTree.age')} id="family-tree-age" name="age" data-cy="age" type="text" />
              <ValidatedField
                label={translate('familyfunApp.familyTree.location')}
                id="family-tree-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('familyfunApp.familyTree.user')}
                id="family-tree-user"
                data-cy="user"
                type="select"
                multiple
                name="users"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="family-tree-photo"
                name="photo"
                data-cy="photo"
                label={translate('familyfunApp.familyTree.photo')}
                type="select"
              >
                <option value="" key="0" />
                {photos
                  ? photos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/family-tree" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FamilyTreeUpdate;
