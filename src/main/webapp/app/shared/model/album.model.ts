import { IFamilyTree } from 'app/shared/model/family-tree.model';
import { IPhoto } from 'app/shared/model/photo.model';

export interface IAlbum {
  id?: number;
  title?: string;
  description?: string | null;
  familyTrees?: IFamilyTree[] | null;
  photos?: IPhoto[] | null;
}

export const defaultValue: Readonly<IAlbum> = {};
