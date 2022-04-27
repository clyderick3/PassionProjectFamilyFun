import { IFamilyTree } from 'app/shared/model/family-tree.model';
import { IAlbum } from 'app/shared/model/album.model';

export interface IPhoto {
  id?: number;
  title?: string | null;
  description?: string | null;
  date?: string | null;
  height?: number | null;
  width?: number | null;
  familyTrees?: IFamilyTree[] | null;
  album?: IAlbum | null;
}

export const defaultValue: Readonly<IPhoto> = {};
