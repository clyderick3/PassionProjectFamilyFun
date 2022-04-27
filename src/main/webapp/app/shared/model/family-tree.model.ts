import { IUser } from 'app/shared/model/user.model';
import { IPhoto } from 'app/shared/model/photo.model';
import { IJournaling } from 'app/shared/model/journaling.model';
import { IAlbum } from 'app/shared/model/album.model';

export interface IFamilyTree {
  id?: number;
  name?: string | null;
  age?: string | null;
  location?: string | null;
  users?: IUser[] | null;
  photo?: IPhoto | null;
  journalings?: IJournaling[] | null;
  albums?: IAlbum[] | null;
}

export const defaultValue: Readonly<IFamilyTree> = {};
